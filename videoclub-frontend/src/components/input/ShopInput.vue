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
      Shop
    </template>
    <b-autocomplete
      v-model="query"
      :custom-formatter="shopToString"
      :data="filteredData"
      :keep-first="true"
      :open-on-focus="true"
      :loading="isLoading"
      icon="store"
      required
      @select="onSelect">
      <template slot-scope="props">
        {{ props.option.city }}
        <br>
        <small class="has-text-grey-light">
          {{ props.option.street }}
        </small>
      </template>
    </b-autocomplete>
  </b-field>
</template>
<script lang="ts">
    import {Component, Prop, Vue, Watch} from 'vue-property-decorator';
    import {Shop} from '@/data/Shop';
    import {ShopModule} from '@/store/modules/shops';
    import Fuse from 'fuse.js';

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
    export default class ShopInput extends Vue {

        // ========== Props ========== //

        @Prop({required: true})
        public value!: number | undefined;


        // ========== Data ========== //

        public data: ReadonlyArray<Shop> = [];
        public query: string = '';
        public isLoading = true;


        // ========== Computed ========== //

        private get fuse() {
            return new Fuse(this.data, {
                threshold: 0.5,
                keys: ['id'], // This is necessary, so `getFn` is called
                getFn: this.shopToString,
            });
        }

        public get filteredData(): ReadonlyArray<Shop> {
            if (this.query.trim()) {
                return this.fuse.search(this.query, {limit: 25});
            } else {
                return this.data;
            }
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            await ShopModule.loadShops();
            this.data = ShopModule.shops;
            this.isLoading = false;

            if (this.value !== undefined) {
                const shop = this.data.find(shop => shop.id === this.value);
                if (shop !== undefined) {
                    this.query = this.shopToString(shop);
                }
            }
        }


        // ========== Methods ========== //

        public shopToString(shop: Shop): string {
            return `${shop.city} - ${shop.street}`;
        }

        public onSelect(shop: Shop) {
            this.$emit('select', shop.id);
        }


        // ========== Watchers ========== //

        @Watch('query')
        protected onInput() {
            const shop = this.data.find(shop => this.shopToString(shop) === this.query);
            if (shop === undefined) {
                this.$emit('select');
            } else if (this.value !== shop.id) {
                this.$emit('select', shop.id);
            }
        }
    };
</script>
