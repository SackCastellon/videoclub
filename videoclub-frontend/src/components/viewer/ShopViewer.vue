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
          :to="{name: 'shop-list'}">
          <b-icon
            icon="chevron-left"
            class="mr-1" />
          <p>Back to list</p>
        </router-link>
        <h1
          class="title"
          :class="{'is-italic has-text-grey-light': isLoading}">
          {{ data ? `Shop #${data.id}` : 'Shop ##' }}
        </h1>
        <hr>
        <b-field>
          <template slot="label">
            Manager
          </template>
          <b-input
            :value="shopManager"
            :loading="isLoading"
            icon="account-tie"
            readonly />
        </b-field>
        <b-field>
          <template slot="label">
            City
          </template>
          <b-input
            :value="shopCity"
            :loading="isLoading"
            icon="city"
            readonly />
        </b-field>
        <b-field>
          <template slot="label">
            Street
          </template>
          <b-input
            :value="shopStreet"
            :loading="isLoading"
            icon="routes"
            readonly />
        </b-field>
        <b-field>
          <template slot="label">
            Zip code
          </template>
          <b-input
            :value="shopZipCode"
            :loading="isLoading"
            icon="numeric"
            readonly />
        </b-field>
        <hr>
        <div class="buttons is-right">
          <b-button
            v-if="isAdmin"
            type="is-primary"
            outlined
            :loading="isLoading"
            @click="onEdit">
            Edit information
          </b-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {UserModule} from '@/store/modules/user';
    import {UserType} from '@/data/User';
    import {Shop} from '@/data/Shop';
    import {ShopModule} from '@/store/modules/shops';

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
    export default class ShopViewer extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: Shop | null = null;


        // ========== Computed ========== //

        public get shopManager(): string {
            return this.data?.manager || '';
        }

        public get shopCity(): string {
            return this.data?.city || '';
        }

        public get shopStreet(): string {
            return this.data?.street || '';
        }

        public get shopZipCode(): string {
            return this.data?.zipCode || '';
        }

        public get isLoading(): boolean {
            return this.data === null;
        }

        public get isAdmin(): boolean {
            return UserModule.user?.type === UserType.ADMIN;
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            const id = parseInt(this.$route.params['id']);
            if (isNaN(id)) {
                this.$buefy.toast.open({message: 'Invalid shop ID', type: 'is-warning'});
                return this.$router.push({name: 'shop-list'});
            }

            const shop = await ShopModule.get(id);
            if (shop) this.data = shop;
            else {
                this.$buefy.toast.open({message: `No shop with ID ${id} was found`, type: 'is-warning'});
                return this.$router.push({name: 'shop-list'});
            }
        }


        // ========== Methods ========== //

        public onEdit() {
            const id = this.data?.id;
            if (id !== undefined) {
                this.$router.push({name: 'shop-edit', params: {id: id.toString()}});
            }
        }


        // ========== Watchers ========== //
    }
</script>
