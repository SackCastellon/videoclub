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
      <b-tooltip
        class="cart-icon"
        :label="cartData.tooltip"
        :active="isActive"
        animated>
        <a
          @click="onCartClicked"
          @mouseenter="onHover(true)"
          @mouseleave="onHover(false)">
          <b-icon
            :icon="cartData.icon"
            :type="cartData.type" />
        </a>
      </b-tooltip>
      <b-tooltip
        class="movie-title mx-2 mb-2"
        :label="title"
        animated>
        <b-button
          expanded
          tag="router-link"
          :to="{name: 'movie-view', params: { id }}"
          type="is-light">
          {{ title }}
        </b-button>
      </b-tooltip>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import {CartModule} from '@/store/modules/cart';

    const BTag = () => import(/* webpackChunkName: "b_tag" */ 'buefy/src/components/tag/Tag.vue');
    const BIcon = () => import(/* webpackChunkName: "b_icon" */ 'buefy/src/components/icon/Icon.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');
    const BTooltip = () => import(/* webpackChunkName: "b_tooltip" */ 'buefy/src/components/tooltip/Tooltip.vue');


    @Component({
        components: {
            BTag,
            BIcon,
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

        private isHovering = false;
        private isActive = true;


        // ========== Computed ========== //

        public get isInCart(): boolean {
            return CartModule.movieIds.find(id => id === this.id) !== undefined;
        }

        public get cartData(): { icon: string, type: string, tooltip: string } {
            if (this.isInCart) {
                if (this.isHovering) {
                    return {
                        icon: 'cart-arrow-up',
                        type: 'is-danger',
                        tooltip: 'Remove from cart',
                    };
                } else {
                    return {
                        icon: 'cart',
                        type: 'is-info',
                        tooltip: 'Remove from cart',
                    };
                }
            } else {
                if (this.isHovering) {
                    return {
                        icon: 'cart-arrow-down',
                        type: 'is-success',
                        tooltip: 'Add to cart',
                    };
                } else {
                    return {
                        icon: 'cart-outline',
                        type: 'is-dark',
                        tooltip: 'Add to cart',
                    };
                }
            }
        }


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //

        public onCartClicked() {
            if (this.isInCart) {
                CartModule.removeFromCart(this.id);
            } else {
                CartModule.addToCart(this.id);
            }
            this.isHovering = false;
            this.isActive = false;
        }

        public onHover(hover: boolean) {
            if (!this.isActive && !this.isHovering && hover) {
                this.isActive = true;
            }
            this.isHovering = hover;
        }


        // ========== Watchers ========== //

    }
</script>

<style scoped lang="scss">
  div.card {
    box-shadow: 0 0.2rem 0.5rem rgba(10, 10, 10, 0.15) !important;

    div.card-image {
      position: relative;

      span.cart-icon {
        top: .5rem;
        right: .5rem;
        position: absolute;
      }

      span.movie-title {
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

