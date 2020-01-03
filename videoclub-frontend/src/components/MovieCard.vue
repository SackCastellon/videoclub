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
    <div class="card-image">
      <figure class="image is-2by3">
        <img
          :src="image"
          :alt="title">
      </figure>
      <b-tooltip :label="title">
        <b-button
          expanded
          tag="router-link"
          :to="{name: 'movie-view', params: { id }}"
          type="is-light"
          class="mx-2 mb-2">
          {{ title }}
        </b-button>
      </b-tooltip>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';

    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');
    const BTooltip = () => import(/* webpackChunkName: "b_tooltip" */ 'buefy/src/components/tooltip/Tooltip.vue');

    @Component({
        components: {
            BButton,
            BTooltip,
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
  div.card {
    box-shadow: 0 0.2rem 0.5rem rgba(10, 10, 10, 0.15) !important;

    div.card-image {
      position: relative;

      span.b-tooltip {
        left: 0;
        right: 0;
        bottom: 0;
        position: absolute;

        a {
          display: block;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }
  }
</style>

