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
  <div class="card">
    <div class="card-image d-flex justify-content-center align-items-center">
      <img :src="image">
      <b-button
        tag="router-link"
        :to="{name: 'view-movie', params: { id }}"
        :exact-active-class="''"
        type="is-light"
        class="mx-2 my-1">
        <h6 class="title is-6">
          {{ title }}
        </h6>
      </b-button>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';

    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');

    @Component({
        components: {
            BButton,
        },
    })
    export default class MovieCard extends Vue {

        // ========== Props ========== //

        @Prop({required: true})
        public id!: number;

        @Prop({required: true})
        public title!: string;

        @Prop({required: true})
        public image!: string;


        // ========== Data ========== //


        // ========== Computed ========== //


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //


        // ========== Watchers ========== //

    }
</script>

<style scoped lang="scss">
  .card {
    box-shadow: 0 0.2rem 0.5rem rgba(10, 10, 10, 0.15) !important;
  }

  .card-image {
    position: relative;
  }

  img {
    width: 300px;
    max-width: 100%;
  }

  a {
    position: absolute;
    left: 0;
    right: 0;
    bottom: .25rem;
  }
</style>

