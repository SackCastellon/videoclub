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
    <h1 class="title is-1 is-spaced">
      Movies
    </h1>
    <h5 class="subtitle is-5">
      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae mauris at nunc sagittis fermentum. Aliquam
      congue orci sed neque ullamcorper egestas. Donec rhoncus massa at elit mattis, ut pellentesque quam porta. Nam
      vestibulum a turpis a finibus. Donec iaculis rhoncus eros quis pulvinar.
    </h5>
    <!--<div class="columns is-centered">
      <div class="column is-10">
        <b-field>
          <b-input
            placeholder="Search"
            type="search"
            icon="magnify" />
          <p class="control">
            <button class="button is-primary">
              Search
            </button>
          </p>
        </b-field>
      </div>
    </div>-->
    <div class="buttons is-right">
      <b-button
        v-if="isAdmin"
        tag="router-link"
        :to="{name: 'movie-create'}"
        type="is-primary"
        outlined>
        New movie
      </b-button>
    </div>
    <hr class="mt-0">
    <div class="columns is-multiline is-mobile">
      <div
        v-for="movie in data"
        :key="movie.id"
        class="column is-half-mobile is-one-third-tablet is-one-quarter-desktop is-one-fifth-widescreen is-2-fullhd">
        <MovieCard :value="movie" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {MovieModule} from '@/store/modules/movies';
    import {Movie} from '@/data/Movie';
    import {UserModule} from '@/store/modules/user';
    import {UserType} from '@/data/User';

    const MovieCard = () => import(/* webpackChunkName: "movie_card" */ '@/components/MovieCard.vue');

    @Component({
        components: {
            MovieCard,
        },
    })
    export default class MovieList extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: ReadonlyArray<Movie> = [];
        public isLoading = true;


        // ========== Computed ========== //

        public get isAdmin(): boolean {
            return UserModule.user?.type === UserType.ADMIN;
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            await MovieModule.load();
            this.data = MovieModule.movies;
            this.isLoading = false;
        }


        // ========== Methods ========== //


        // ========== Watchers ========== //
    };
</script>
