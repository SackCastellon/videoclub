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
  <section class="section">
    <div class="container">
      <h1 class="title is-1 is-spaced">
        Movies
      </h1>
      <h5 class="subtitle is-5">
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae mauris at nunc sagittis fermentum. Aliquam
        congue orci sed neque ullamcorper egestas. Donec rhoncus massa at elit mattis, ut pellentesque quam porta. Nam
        vestibulum a turpis a finibus. Donec iaculis rhoncus eros quis pulvinar.
      </h5>
      <hr>
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
    </div>
  </section>
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
