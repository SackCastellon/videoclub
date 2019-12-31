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
  <div class="tile is-parent px-0">
    <div class="tile is-child notification">
      <div class="level is-mobile">
        <div class="level-left">
          <h1 class="title is-3">
            {{ title }}
          </h1>
        </div>
        <div class="level-right">
          <b-button
            type="is-info"
            tag="router-link"
            :to="link">
            View more
          </b-button>
        </div>
      </div>
      <div
        class="d-flex align-items-center"
        style="position: relative">
        <a
          class="navigation"
          style="left: 0"
          :disabled="!canGoPrev"
          @click="goPrev">
          <b-icon
            size="is-large"
            type="is-primary"
            icon="chevron-left" />
        </a>
        <carousel
          ref="carousel"
          :center-mode="true"
          :scroll-per-page="false"
          :pagination-enabled="false"
          :per-page-custom="perPageCustom">
          <slide
            v-for="movie in movies"
            :key="movie"
            class="d-flex justify-content-center align-items-center">
            <MovieCard
              :id="movie"
              :title="`Movie ${movie}`"
              :image="`https://via.placeholder.com/200x300.png?text=Movie ${movie}`"
              class="movie" />
          </slide>
        </carousel>
        <a
          class="navigation"
          style="right: 0"
          :disabled="!canGoNext"
          @click="goNext">
          <b-icon
            size="is-large"
            type="is-primary"
            icon="chevron-right" />
        </a>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import {RawLocation} from 'vue-router';

    const Slide = () => import(/* webpackChunkName: "slide" */ 'vue-carousel/src/Slide.vue');
    const Carousel = () => import(/* webpackChunkName: "carousel" */ 'vue-carousel/src/Carousel.vue');
    const MovieCard = () => import(/* webpackChunkName: "movie_card" */ '@/components/MovieCard.vue');

    @Component({
        components: {
            Slide,
            Carousel,
            MovieCard,
        },
    })
    export default class MovieCarousel extends Vue {

        // ========== Props ========== //

        @Prop({required: true})
        public title!: string;
        @Prop({required: true})
        public link!: RawLocation;
        @Prop({required: true})
        public movies!: number[]; // TODO


        // ========== Data ========== //

        public perPageCustom = [[0, 2], [768, 4], [990, 5], [1215, 6]];

        public canGoPrev: boolean = false;
        public canGoNext: boolean = true;


        // ========== Computed ========== //


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public mounted() {
            let intervalID = -1;

            // Hack
            intervalID = setInterval(() => {
                const carousel = this.$refs.carousel as any;

                if (carousel) {
                    clearInterval(intervalID);
                    this.canGoPrev = carousel.canAdvanceBackward;
                    this.canGoNext = carousel.canAdvanceForward;
                    carousel.$on('page-change', () => {
                        this.canGoPrev = carousel.canAdvanceBackward;
                        this.canGoNext = carousel.canAdvanceForward;
                    });
                }
            }, 100);
        }


        // ========== Methods ========== //

        public goPrev() {
            if (this.canGoPrev) (this.$refs.carousel as any).advancePage('backward');
        }

        public goNext() {
            if (this.canGoNext) (this.$refs.carousel as any).advancePage();
        }


        // ========== Watchers ========== //

    };
</script>

<style scoped lang="scss">
  a.navigation {
    z-index: 100;
    position: absolute;

    &[disabled] {
      opacity: .5;
      cursor: not-allowed;
    }
  }

  .movie {
    margin: .1rem .35rem .7rem .35rem;
  }
</style>
