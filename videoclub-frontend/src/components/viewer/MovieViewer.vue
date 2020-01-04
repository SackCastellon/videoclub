<!--
  - Copyright 2019 Juan José González Abril
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
      <div class="column is-10-tablet is-6-desktop">
        <h1
          class="title"
          :class="{'is-italic has-text-grey-light': isLoading}">
          {{ movieTitle }}
        </h1>
        <hr>
        <b-field>
          <template slot="label">
            Title
          </template>
          <b-input
            :value="movieTitle"
            :loading="isLoading"
            icon="movie"
            readonly />
        </b-field>
        <b-field>
          <template slot="label">
            Director
          </template>
          <b-input
            :value="movieDirector"
            :loading="isLoading"
            icon="account-tie"
            readonly />
        </b-field>
        <b-field>
          <template slot="label">
            Release date
          </template>
          <b-input
            :value="movieReleaseDate"
            :loading="isLoading"
            icon="calendar-today"
            readonly />
        </b-field>
        <b-field>
          <template slot="label">
            Shop
          </template>
          <b-input
            :value="shopName"
            :loading="isLoading"
            icon="store"
            readonly />
        </b-field>
        <b-field>
          <template slot="label">
            Price
          </template>
          <b-input
            :value="moviePrice"
            :loading="isLoading"
            icon="currency-eur"
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
          <b-button
            v-if="isInCart"
            type="is-danger"
            :loading="isLoading"
            @click="onRemoveFromCart">
            Remove from cart
          </b-button>
          <b-button
            v-else
            type="is-primary"
            :loading="isLoading"
            @click="onAddToCart">
            Add to cart
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
    import {Movie} from '@/data/Movie';
    import {dateFormat, moment} from '@/util/Moment';
    import {MovieModule} from '@/store/modules/movies';
    import {CartModule} from '@/store/modules/cart';
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
    export default class MovieViewer extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: Movie | null = null;

        public shopName: string = '';

        public isLoading: boolean = true;


        // ========== Computed ========== //

        public get movieTitle(): string {
            return this.data?.name || 'Movie title';
        }

        public get movieDirector(): string {
            return this.data?.director || '';
        }

        public get movieReleaseDate(): string {
            if (this.data) {
                return moment(this.data.releaseDate).format(dateFormat);
            } else {
                return '';
            }
        }

        public get moviePrice(): string {
            if (this.data) {
                return this.data.price.toFixed(2) + ' €';
            } else {
                return '';
            }
        }

        public get isAdmin(): boolean {
            return UserModule.user?.type === UserType.ADMIN;
        }

        public get isInCart(): boolean {
            if (this.data) {
                return CartModule.has(this.data);
            } else {
                return false;
            }
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            const id = parseInt(this.$route.params['id']);
            if (isNaN(id)) {
                this.$buefy.toast.open({message: 'Invalid movie ID', type: 'is-warning'});
                return this.$router.push({name: 'movie-list'});
            }

            const movie = await MovieModule.get(id);
            if (movie) {
                this.data = movie;
                const shop = (await ShopModule.get(movie.shopId))!!;
                this.shopName = `${shop.city} - ${shop.street}`;
            } else {
                this.$buefy.toast.open({message: `No movie with ID ${id} was found`, type: 'is-warning'});
                return this.$router.push({name: 'movie-list'});
            }

            this.isLoading = false;
        }


        // ========== Methods ========== //

        public onEdit() {
            if (this.data) {
                this.$router.push({name: 'movie-edit', params: {id: this.data.id.toString()}});
            }
        }

        public onAddToCart() {
            if (this.data) {
                CartModule.add(this.data);
            }
        }

        public onRemoveFromCart() {
            if (this.data) {
                CartModule.remove(this.data);
            }
        }


        // ========== Watchers ========== //
    }
</script>
