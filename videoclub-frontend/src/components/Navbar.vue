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
  <b-navbar
    id="navbar"
    :fixed-top="true"
    :shadow="true"
    wrapper-class="container">
    <template v-slot:brand>
      <b-navbar-item
        tag="router-link"
        :to="{ path: '/' }">
        <h1 class="title is-4">
          Videoclub
        </h1>
      </b-navbar-item>
    </template>
    <template v-slot:start>
      <b-navbar-item
        tag="router-link"
        :to="{ name: 'home' }">
        Home
      </b-navbar-item>
    </template>
    <template v-slot:end>
      <b-navbar-dropdown
        ref="dropdown"
        right
        boxed>
        <template v-slot:label>
          <div
            class="d-flex align-items-center">
            <b-icon
              :icon="isAuthenticated ? 'account-circle' : 'account-circle-outline'"
              class="mr-1" />
            <p>
              Account
            </p>
          </div>
        </template>
        <template v-if="isAuthenticated">
          <b-navbar-item tag="div">
            <p>
              Signed in as <strong>{{ username }}</strong>
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
    import {UserModule} from '@/store/modules/user';
    import {logout} from '@/api/auth';

    const BIcon = () => import(/* webpackChunkName: "b_icon" */ 'buefy/src/components/icon/Icon.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');
    const BNavbar = () => import(/* webpackChunkName: "b_navbar" */ 'buefy/src/components/navbar/Navbar.vue');
    const BNavbarItem = () => import(/* webpackChunkName: "b_navbar_item" */ 'buefy/src/components/navbar/NavbarItem.vue');
    const BNavbarDropdown = () => import(/* webpackChunkName: "b_navbar_dropdown" */ 'buefy/src/components/navbar/NavbarDropdown.vue');

    @Component({
        components: {
            BIcon,
            BButton,
            BNavbar,
            BNavbarItem,
            BNavbarDropdown,
        },
    })
    export default class Navbar extends Vue {

        // ========== Data ========== //


        // ========== Computed ========== //

        public get username(): string | null {
            return UserModule.username;
        }

        public get isAuthenticated(): boolean {
            return AuthModule.isAuthenticated;
        }


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //

        public closeMenu() {
            (this.$refs.dropdown as any).closeMenu();
        }

        public logout() {
            this.closeMenu();
            logout();
        }


        // ========== Watchers ========== //

        @Watch('isAuthenticated', {immediate: true})
        public onAuthenticated() {
            UserModule.loadInfo();
        }
    }
</script>

<style lang="scss" scoped>
  #navbar {
    box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.15) !important;
  }
</style>
