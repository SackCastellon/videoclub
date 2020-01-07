<!--
  - Copyright 2020 Juan José González Abril
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <b-field>
    <template slot="label">
      User
    </template>
    <b-input
      v-if="readonly"
      :value="query"
      :loading="isLoading"
      icon="store"
      readonly />
    <b-autocomplete
      v-else
      v-model="query"
      :custom-formatter="userToString"
      :data="filteredData"
      :keep-first="true"
      :open-on-focus="true"
      :loading="isLoading"
      icon="account"
      required
      @select="onSelect">
      <template slot-scope="props">
        {{ props.option.username }}
      </template>
    </b-autocomplete>
  </b-field>
</template>
<script lang="ts">
    import {Component, Prop, Vue, Watch} from 'vue-property-decorator';
    import {User} from '@/data/User';
    import Fuse from 'fuse.js';
    import {getUsers} from '@/api/modules/user';
    import {converter} from '@/util/JsonConverter';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BAutocomplete = () => import(/* webpackChunkName: "b_autocomplete" */ 'buefy/src/components/autocomplete/Autocomplete.vue');

    @Component({
        components: {
            BField,
            BAutocomplete,
        },
        model: {
            prop: 'value',
            event: 'select',
        },
    })
    export default class UserInput extends Vue {

        // ========== Props ========== //

        @Prop({required: true})
        public value!: number | undefined;

        @Prop({default: false})
        public readonly!: boolean;


        // ========== Data ========== //

        public data: ReadonlyArray<User> = [];
        public query: string = '';
        public isLoading = true;


        // ========== Computed ========== //

        private get fuse() {
            return new Fuse(this.data, {
                threshold: 0.5,
                keys: ['id'], // This is necessary, so `getFn` is called
                getFn: this.userToString,
            });
        }

        public get filteredData(): ReadonlyArray<User> {
            if (this.query.trim()) {
                return this.fuse.search(this.query, {limit: 25});
            } else {
                return this.data;
            }
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            this.data = converter.deserializeArray((await getUsers()).data, User);
            this.isLoading = false;
        }


        // ========== Methods ========== //

        public userToString(user: User): string {
            return user.username;
        }

        public onSelect(user: User) {
            this.$emit('select', user.id);
        }


        // ========== Watchers ========== //

        @Watch('query')
        protected onInput() {
            const user = this.data.find(user => this.userToString(user) === this.query);
            if (user === undefined) {
                this.$emit('select');
            } else if (this.value !== user.id) {
                this.$emit('select', user.id);
            }
        }
    };
</script>
