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
  <div id="app">
    <Navbar />
    <transition
      mode="out-in"
      name="fade">
      <router-view />
    </transition>
  </div>
</template>

<script lang="ts">
    import {Component, Vue, Watch} from 'vue-property-decorator';
    import {AuthModule} from '@/store/modules/auth';
    import {nextAuthorizedRoute} from '@/router';

    const Navbar = () => import(/* webpackChunkName: "navbar" */ '@/components/Navbar.vue');

    @Component({
        components: {
            Navbar,
        },
    })
    export default class App extends Vue {

        // ========== Data ========== //


        // ========== Computed ========== //

        public get isAuthenticated(): boolean {
            return AuthModule.isAuthenticated;
        }


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //


        // ========== Watchers ========== //

        @Watch('isAuthenticated')
        public onAuthenticated(authenticated: boolean) {
            if (!authenticated) this.$router.push(nextAuthorizedRoute(this.$route));
        }
    }
</script>
