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
    <section class="section">
      <h1 class="title is-1">
        Movies
      </h1>
    </section>
    <section class="section">
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
      <div class="columns is-multiline is-mobile">
        <div
          v-for="movie in movies"
          :key="movie"
          class="column is-6-mobile is-4-tablet is-one-fifth-desktop">
          <MovieCard
            :id="movie"
            :title="`Movie ${movie}`"
            :image="`https://via.placeholder.com/200x300.png?text=Movie ${movie}`" />
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    const MovieCard = () => import(/* webpackChunkName: "movie_card" */ '@/components/MovieCard.vue');

    @Component({
        components: {
            MovieCard,
        },
    })
    export default class Movies extends Vue {

        // ========== Props ========== //

        public movies = [...Array(25).keys()]; // TODO: Remove


        // ========== Data ========== //


        // ========== Computed ========== //


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //


        // ========== Watchers ========== //
    };
</script>

<style scoped lang="scss">
  img {
    width: 300px;
    max-width: 100%;
  }
</style>
