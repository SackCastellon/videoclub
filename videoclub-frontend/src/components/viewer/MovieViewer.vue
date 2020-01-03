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
            tag="router-link"
            :to="{name:'movie-edit', params: {id: data.id}}"
            type="is-primary"
            outlined>
            Edit
          </b-button>
          <b-button
            v-else
            type="is-primary">
            Rent
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


        // ========== Computed ========== //

        public get movieTitle(): string {
            return this.data?.name || 'Movie title';
        }

        public get movieDirector(): string {
            return this.data?.director || '';
        }

        public get movieReleaseDate(): string {
            const date = this.data?.releaseDate;
            if (date) return moment(date).format(dateFormat);
            else return '';
        }

        public get moviePrice(): string {
            const price = this.data?.price;
            if (price) return price.toFixed(2) + ' €';
            else return '';
        }

        public get isLoading(): boolean {
            return this.data === null;
        }

        public get isAdmin(): boolean {
            return UserModule.data?.type === UserType.ADMIN;
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            const id = parseInt(this.$route.params['id']);
            if (isNaN(id)) {
                this.$buefy.toast.open({message: 'Invalid movie ID', type: 'is-warning'});
                return this.$router.push({name: 'movie-list'});
            }

            const movie = await MovieModule.getMovie(id);
            if (movie) this.data = movie;
            else {
                this.$buefy.toast.open({message: `No movie with ID ${id} was found`, type: 'is-warning'});
                return this.$router.push({name: 'movie-list'});
            }
        }


        // ========== Methods ========== //


        // ========== Watchers ========== //
    }
</script>
