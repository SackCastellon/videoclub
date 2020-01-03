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
  <div
    class="tile is-parent px-0">
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
            :to="{name: 'movie-list', query: { sort: type }}">
            View more
          </b-button>
        </div>
      </div>
      <b-carousel-list
        v-if="data.length > 0"
        ref="carousel"
        style="box-shadow: initial"
        :data="data"
        :items-to-list="1"
        :arrow-hover="false"
        :config="carouselConfig">
        <template
          slot="item"
          slot-scope="props">
          <MovieCard
            :id="props.list.id"
            :title="props.list.name"
            :image="`https://via.placeholder.com/200x300.png`"
            class="m-1 mb-2" />
        </template>
      </b-carousel-list>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import {Movie} from '@/data/Movie';
    import {MovieModule} from '@/store/modules/movies';

    const BCarouselList = () => import(/* webpackChunkName: "b_carousel_list" */ 'buefy/src/components/carousel/CarouselList.vue');
    const BLoading = () => import(/* webpackChunkName: "b_loading" */ 'buefy/src/components/loading/Loading.vue');
    const MovieCard = () => import(/* webpackChunkName: "movie_card" */ '@/components/MovieCard.vue');

    @Component({
        components: {
            BCarouselList,
            BLoading,
            MovieCard,
        },
    })
    export default class MovieCarousel extends Vue {

        // ========== Props ========== //

        @Prop({required: true})
        public title!: string;
        @Prop({required: true})
        public type!: 'new' | 'top';


        // ========== Data ========== //

        public data: ReadonlyArray<Movie> = [];

        public carouselConfig = {
            itemsToShow: 2,
            breakpoints: {
                768: {
                    itemsToShow: 3,
                },
                1023: {
                    itemsToShow: 4,
                },
                1200: {
                    itemsToShow: 5,
                },
            },
        };


        // ========== Computed ========== //


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            switch (this.type) {
                case 'new':
                    await MovieModule.loadNewMovies();
                    this.data = MovieModule.newMovies;
                    break;
                case 'top':
                    await MovieModule.loadTopMovies();
                    this.data = MovieModule.topMovies;
                    break;
            }

            this.$nextTick(() => {
                let intervalId = 0;
                intervalId = setInterval(() => {
                    if (this.$refs.carousel && intervalId !== 0) {
                        clearInterval(intervalId);
                        intervalId = 0;

                        this.updateCarousel();
                    }
                }, 100);
            });

            if (typeof window !== 'undefined') {
                window.addEventListener('resize', this.updateCarousel);
            }
        }

        // noinspection JSUnusedGlobalSymbols
        public beforeDestroy() {
            window.removeEventListener('resize', this.updateCarousel);
        }


        // ========== Methods ========== //

        public updateCarousel() {
            const carousel = this.$refs.carousel as any;
            const itemsToShow: number = carousel.settings.itemsToShow;

            carousel.total = this.data.length - (itemsToShow);

            this.$nextTick(() => {
                // noinspection JSDeprecatedSymbols
                carousel.update();
            });
        }


        // ========== Watchers ========== //

    };
</script>

<style scoped lang="scss">
  div.tile.notification {
    max-width: 100%; // Fixes exponentially increasing size when resizing window
  }
</style>
