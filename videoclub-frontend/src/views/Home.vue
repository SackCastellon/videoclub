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
        Videoclub
      </h1>
    </section>
    <MovieCarousel
      title="New movies"
      view-more-link="/" />
    <MovieCarousel
      title="Popular movies"
      view-more-link="/" />
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    const MovieCarousel = () => import(/* webpackChunkName: "movie_carousel" */ '@/views/MovieCarousel.vue');

    @Component({
        components: {
            MovieCarousel,
        },
    })
    export default class Home extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //


        // ========== Computed ========== //


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //


        // ========== Watchers ========== //

    }
</script>

