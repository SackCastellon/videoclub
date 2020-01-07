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
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-9-tablet is-8-desktop is-7-widescreen is-6-fullhd">
        <router-link
          class="button mb-3"
          :to="{name: 'stat-list'}">
          <b-icon
            icon="chevron-left"
            class="mr-1" />
          <p>Back to list</p>
        </router-link>
        <h1
          class="title"
          :class="{'is-italic has-text-grey-light': isLoading}">
          {{ data ? `Stat #${data.id}` : 'Stat ##' }}
        </h1>
        <hr>
        <b-field>
          <template slot="label">
            Member
          </template>
          <b-input
            :value="memberName"
            :loading="isLoading"
            icon="account"
            readonly />
        </b-field>

        <b-field>
          <template slot="label">
            Creation date
          </template>
          <b-input
            :value="statCreationDate"
            :loading="isLoading"
            icon="calendar"
            readonly />
        </b-field>

        <b-field>
          <template slot="label">
            Total spent
          </template>
          <b-input
            :value="statTotalSpent"
            :loading="isLoading"
            icon="currency-eur"
            readonly />
        </b-field>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {Stat} from '@/data/Stat';
    import {StatModule} from '@/store/modules/stats';
    import {moment} from '@/util/Moment';
    import {getUser} from '@/api/modules/user';
    import {converter} from '@/util/JsonConverter';
    import {User} from '@/data/User';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BInput = () => import(/* webpackChunkName: "b_input" */ 'buefy/src/components/input/Input.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');

    @Component({
        components: {
            BField,
            BInput,
            BButton,
        },
    })
    export default class StatViewer extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: Stat | null = null;
        public memberName: string = '';


        // ========== Computed ========== //

        public get statCreationDate(): string {
            if (this.data) {
                return moment(this.data.creationDate).format('MM/YYYY');
            } else {
                return '';
            }
        }

        public get statTotalSpent(): string {
            return this.data?.totalSpent.toFixed(2) + ' €' || '';
        }

        public get isLoading(): boolean {
            return this.data === null;
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            const id = parseInt(this.$route.params['id']);
            if (isNaN(id)) {
                this.$buefy.toast.open({message: 'Invalid stat ID', type: 'is-warning'});
                return this.$router.push({name: 'stat-list'});
            }

            const stat = await StatModule.get(id);
            if (stat) {
                this.data = stat;
                this.memberName = converter.deserializeObject((await getUser(stat.memberId)).data, User).username;
            } else {
                this.$buefy.toast.open({message: `No stat with ID ${id} was found`, type: 'is-warning'});
                return this.$router.push({name: 'stat-list'});
            }
        }


        // ========== Methods ========== //


        // ========== Watchers ========== //
    }
</script>
