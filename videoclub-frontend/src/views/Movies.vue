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
  <section class="section">
    <transition
      mode="out-in"
      name="fade">
      <MovieList v-if="routeName === 'movie-list'" />
      <MovieViewer v-else-if="routeName === 'movie-view'" />
      <MovieEditor v-else-if="routeName === 'movie-edit' || routeName === 'movie-create'" />
    </transition>
  </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    const MovieList = () => import(/* webpackChunkName: "movie_list" */ '@/components/list/MovieList.vue');
    const MovieViewer = () => import(/* webpackChunkName: "movie_viewer" */ '@/components/viewer/MovieViewer.vue');
    const MovieEditor = () => import(/* webpackChunkName: "movie_editor" */ '@/components/editor/MovieEditor.vue');

    @Component({
        components: {
            MovieList,
            MovieViewer,
            MovieEditor,
        },
    })
    export default class Movies extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //


        // ========== Computed ========== //

        public get routeName(): string {
            return this.$route.name || '';
        }


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //


        // ========== Watchers ========== //
    };
</script>

<style scoped lang="scss">
</style>
