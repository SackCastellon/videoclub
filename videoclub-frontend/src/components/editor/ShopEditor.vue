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
    <form
      action=""
      method="post">
      <div class="columns is-centered">
        <div class="column is-10-tablet is-6-desktop">
          <header>
            <h1 class="title">
              {{ isEdit ? 'Editing: ' : 'New shop' }}
              <i class="has-text-grey">{{ `Shop #${data.id}` }}</i>
            </h1>
          </header>
          <hr>
          <section>
            <b-field>
              <template slot="label">
                Manager
              </template>
              <b-input
                v-model="data.manager"
                :loading="isLoading"
                icon="account-tie"
                required />
            </b-field>
            <b-field>
              <template slot="label">
                City
              </template>
              <b-input
                v-model="data.city"
                :loading="isLoading"
                icon="city"
                required />
            </b-field>
            <b-field>
              <template slot="label">
                Street
              </template>
              <b-input
                v-model="data.street"
                :loading="isLoading"
                icon="routes"
                required />
            </b-field>
            <b-field>
              <template slot="label">
                Zip code
              </template>
              <b-input
                v-model="data.zipCode"
                :loading="isLoading"
                icon="numeric"
                required />
            </b-field>
          </section>
          <hr>
          <footer class="buttons is-right">
            <b-button
              type="is-success"
              @click="onSave">
              Save
            </b-button>
            <b-button
              type="is-danger"
              @click="onCancel">
              Cancel
            </b-button>
          </footer>
        </div>
      </div>
    </form>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {Shop} from '@/data/Shop';
    import cloneDeep from 'lodash.clonedeep';
    import {ShopModule} from '@/store/modules/shops';
    import {postShop} from '@/api/shops';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BInput = () => import(/* webpackChunkName: "b_input" */ 'buefy/src/components/input/Input.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');
    const BDatepicker = () => import(/* webpackChunkName: "b_datepicker" */ 'buefy/src/components/datepicker/Datepicker.vue');
    const BNumberinput = () => import(/* webpackChunkName: "b_numberinput" */ 'buefy/src/components/numberinput/Numberinput.vue');
    const BAutocomplete = () => import(/* webpackChunkName: "b_autocomplete" */ 'buefy/src/components/autocomplete/Autocomplete.vue');

    @Component({
        components: {
            BField,
            BInput,
            BButton,
            BDatepicker,
            BNumberinput,
            BAutocomplete,
        },
    })
    export default class ShopEditor extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: Shop = new Shop();
        public isLoading: boolean = true;


        // ========== Computed ========== //

        public get isEdit(): boolean {
            return this.$route.name === 'shop-edit';
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            if (this.isEdit) {
                const id = parseInt(this.$route.params['id']);
                if (isNaN(id)) {
                    this.$buefy.toast.open({message: 'Invalid shop ID', type: 'is-warning'});
                    return this.$router.push({name: 'shop-list'});
                }

                const shop = await ShopModule.get(id);

                if (shop) this.data = cloneDeep(shop);
                else {
                    this.$buefy.toast.open({message: `No shop with ID ${id} was found`, type: 'is-warning'});
                    return this.$router.push({name: 'shop-list'});
                }
            }
            this.isLoading = false;
        }


        // ========== Methods ========== //

        public onCancel() {
            this.$buefy.dialog.confirm({
                title: 'Close editor',
                message: 'Are you sure you want close the editor?<br>Any unsaved data will be lost.',
                cancelText: 'Keep editing',
                confirmText: 'Close editor',
                type: 'is-danger',
                hasIcon: true,
                onConfirm: () => {
                    if (this.isEdit) {
                        this.$router.push({name: 'shop-view', params: {id: this.data.id.toString()}});
                    } else {
                        this.$router.push({name: 'shop-list'});
                    }
                },
            });
        }

        public async onSave() {
            if (this.isEdit) {
                // TODO
            } else {
                const response = await postShop(this.data);
                const {shopId} = response.data;
                await this.$router.push({name: 'shop-view', params: {id: shopId}});
            }
        }


        // ========== Watchers ========== //
    }
</script>

<style scoped>

</style>
