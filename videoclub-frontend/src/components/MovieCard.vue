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
  <div class="card shadow-sm">
    <div class="card-image">
      <figure class="image is-2by3">
        <img
          :src="`https://placeimg.com/200/300/nature#${value.id}`"
          :alt="value.name">
      </figure>
      <b-tooltip
        v-if="isAdmin"
        class="edit-icon"
        label="Edit movie"
        animated>
        <b-button
          :to="{name: 'movie-edit', params: { id: value.id }}"
          tag="router-link"
          type="is-light"
          class="shadow-sm">
          <b-icon icon="pencil" />
        </b-button>
      </b-tooltip>
      <b-tooltip
        class="cart-icon"
        :label="cartData.tooltip"
        :active="isActive"
        animated>
        <b-button
          tag="a"
          type="is-light"
          class="shadow-sm"
          @click="onCartClicked"
          @mouseenter="onHover(true)"
          @mouseleave="onHover(false)">
          <b-icon
            :icon="cartData.icon"
            :type="cartData.type" />
        </b-button>
      </b-tooltip>
      <b-tooltip
        class="movie-title"
        :label="value.name"
        animated>
        <b-button
          :to="{name: 'movie-view', params: { id: value.id }}"
          tag="router-link"
          type="is-light"
          class="shadow-sm"
          expanded>
          {{ value.name }}
        </b-button>
      </b-tooltip>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import {CartModule} from '@/store/modules/cart';
    import {Movie} from '@/data/Movie';
    import {UserModule} from '@/store/modules/user';
    import {UserType} from '@/data/User';

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
        public value!: Movie;


        // ========== Data ========== //

        private isHovering = false;
        private isActive = true;


        // ========== Computed ========== //

        public get isAdmin(): boolean {
            return UserModule.user?.type === UserType.ADMIN;
        }

        public get isInCart(): boolean {
            return CartModule.has(this.value);
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
                CartModule.remove(this.value);
            } else {
                CartModule.add(this.value);
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
    div.card-image {
      position: relative;

      span.edit-icon {
        top: .3rem;
        left: .3rem;
        position: absolute;
      }

      span.cart-icon {
        top: .3rem;
        right: .3rem;
        position: absolute;
      }

      span.movie-title {
        left: .3rem;
        right: .3rem;
        bottom: .3rem;
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

