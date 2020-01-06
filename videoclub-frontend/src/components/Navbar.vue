import {UserType} from '@/data/User';
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
  <b-navbar
    id="navbar"
    class="is-light"
    :fixed-top="true"
    wrapper-class="container">
    <template v-slot:brand>
      <b-navbar-item
        href="/">
        <h1 class="title is-4">
          Videoclub
        </h1>
      </b-navbar-item>
    </template>
    <template v-slot:start>
      <b-navbar-item
        tag="router-link"
        :to="{ name: 'home' }">
        <div class="d-flex align-items-center">
          <b-icon
            icon="home"
            class="mr-1" />
          <p>
            Home
          </p>
        </div>
      </b-navbar-item>
      <b-navbar-item
        tag="router-link"
        :to="{ name: 'movie-list' }">
        <div class="d-flex align-items-center">
          <b-icon
            icon="movie"
            class="mr-1" />
          <p>
            Movies
          </p>
        </div>
      </b-navbar-item>
      <b-navbar-item
        tag="router-link"
        :to="{ name: 'shop-list' }">
        <div class="d-flex align-items-center">
          <b-icon
            icon="store"
            class="mr-1" />
          <p>
            Shops
          </p>
        </div>
      </b-navbar-item>
    </template>
    <template v-slot:end>
      <b-navbar-item
        tag="router-link"
        :to="{ name: 'cart' }">
        <b-loading
          :is-full-page="false"
          :active="isLoadingCart">
          <div class="cart-loading-icon" />
        </b-loading>
        <div class="d-flex align-items-center">
          <b-icon
            icon="cart"
            class="mr-1" />
          <b-tag
            type="is-white"
            rounded>
            <b class="has-text-info">{{ cartCount }}</b>
          </b-tag>
        </div>
      </b-navbar-item>
      <b-navbar-dropdown
        ref="dropdown"
        right
        boxed>
        <template v-slot:label>
          <div class="d-flex align-items-center">
            <b-icon
              :icon="accountIcon"
              :type="isAuthenticated ? 'is-primary' : ''"
              class="mr-1" />
            <p>
              Account
            </p>
          </div>
        </template>
        <template v-if="isAuthenticated">
          <b-navbar-item tag="div">
            <div>
              <p>
                Signed in as <strong>{{ username }}</strong>
              </p>
              <b-tag
                v-if="isAdmin"
                type="is-primary"
                class="mt-2">
                Admin
              </b-tag>
            </div>
          </b-navbar-item>
          <hr class="navbar-divider">
          <b-navbar-item
            tag="router-link"
            :to="{ name: 'profile-view' }"
            class="d-flex align-items-center"
            @click.native="closeMenu">
            <b-icon
              icon="account"
              class="mr-1" />
            <p>
              Profile
            </p>
          </b-navbar-item>
          <hr class="navbar-divider">
          <b-navbar-item
            class="d-flex align-items-center"
            @click="logout">
            <b-icon
              icon="logout-variant"
              class="mr-1"
              type="is-danger" />
            <p class="has-text-danger">
              Sign out
            </p>
          </b-navbar-item>
        </template>
        <template v-else>
          <b-navbar-item
            tag="router-link"
            :to="{ name: 'login' }"
            class="d-flex align-items-center"
            @click.native="closeMenu">
            <b-icon
              icon="login-variant"
              class="mr-1" />
            <p>
              Sign in
            </p>
          </b-navbar-item>
          <hr class="navbar-divider">
          <b-navbar-item
            tag="router-link"
            :to="{ name: 'register' }"
            class="d-flex align-items-center"
            @click.native="closeMenu">
            <b-icon
              icon="account-plus"
              class="mr-1"
              type="is-primary" />
            <p class="has-text-primary has-text-weight-bold">
              Register
            </p>
          </b-navbar-item>
        </template>
      </b-navbar-dropdown>
    </template>
  </b-navbar>
</template>

<script lang="ts">
    import {Component, Vue, Watch} from 'vue-property-decorator';
    import {AuthModule} from '@/store/modules/auth';
    import {logout} from '@/api/modules/auth';
    import {UserModule} from '@/store/modules/user';
    import {UserType} from '@/data/User';
    import {CartModule} from '@/store/modules/cart';

    const BNavbar = () => import(/* webpackChunkName: "b_navbar" */ 'buefy/src/components/navbar/Navbar.vue');
    const BNavbarItem = () => import(/* webpackChunkName: "b_navbar_item" */ 'buefy/src/components/navbar/NavbarItem.vue');
    const BNavbarDropdown = () => import(/* webpackChunkName: "b_navbar_dropdown" */ 'buefy/src/components/navbar/NavbarDropdown.vue');

    const BTag = () => import(/* webpackChunkName: "b_tag" */ 'buefy/src/components/tag/Tag.vue');
    const BIcon = () => import(/* webpackChunkName: "b_icon" */ 'buefy/src/components/icon/Icon.vue');
    const BLoading = () => import(/* webpackChunkName: "b_loading" */ 'buefy/src/components/loading/Loading.vue');

    @Component({
        components: {
            BNavbar,
            BNavbarItem,
            BNavbarDropdown,
            BTag,
            BIcon,
            BLoading,
        },
    })
    export default class Navbar extends Vue {

        // ========== Data ========== //

        public isLoadingCart: boolean = true;


        // ========== Computed ========== //

        public get cartCount() {
            return CartModule.count;
        }

        public get username(): string {
            return UserModule.user?.username || '';
        }

        public get isAdmin(): boolean {
            return UserModule.user?.type === UserType.ADMIN;
        }

        public get isAuthenticated(): boolean {
            return AuthModule.isAuthenticated;
        }

        public get accountIcon(): string {
            if (this.isAuthenticated) {
                if (this.isAdmin) {
                    return 'shield-account';
                } else {
                    return 'account-circle';
                }
            } else {
                return 'account-circle-outline';
            }
        }


        // ========== Lifecycle Hooks ========== //

        public async created() {
            await CartModule.load();
            this.isLoadingCart = false;
        }


        // ========== Methods ========== //

        public closeMenu() {
            (this.$refs.dropdown as any).closeMenu();
        }

        public async logout() {
            this.closeMenu();
            await logout();
            this.$buefy.toast.open({
                message: 'Signed out successfully',
            });
        }


        // ========== Watchers ========== //

        @Watch('isAuthenticated', {immediate: true})
        public onAuthenticated() {
            UserModule.load();
        }
    }
</script>

<style lang="scss" scoped>
  #navbar {
    box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.15) !important;

    .loading-overlay .cart-loading-icon {
      position: relative;
    }

    .loading-overlay .cart-loading-icon:after {
      animation: spinAround 500ms infinite linear;
      border-radius: 290486px;
      content: "";
      display: block;
      top: calc(50% - 1em);
      left: calc(50% - 1em);
      width: 2em;
      height: 2em;
      border: 0.16em solid transparent;
      border-bottom-color: #dbdbdb;
      border-left-color: #dbdbdb;
    }
  }
</style>
