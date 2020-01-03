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
        Videoclub
      </h1>
      <h5 class="subtitle is-5">
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam ultricies arcu at nibh tristique, ac accumsan
        ligula commodo. Fusce et felis vitae nisl egestas elementum. Maecenas tincidunt, erat in mattis bibendum, lacus
        nisl gravida augue, laoreet blandit odio dolor eget augue.
      </h5>
      <MovieCarousel
        title="New movies"
        type="new" />
      <MovieCarousel
        title="Popular movies"
        type="top" />
    </div>
  </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    const MovieCarousel = () => import(/* webpackChunkName: "movie_carousel" */ '@/components/MovieCarousel.vue');

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

