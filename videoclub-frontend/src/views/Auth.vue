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
    <div class="container">
      <transition
        mode="out-in"
        name="fade">
        <Login
          v-if="routeName === 'login'"
          @success="onLogin" />
        <Register
          v-else-if="routeName === 'register'"
          @success="onRegister" />
      </transition>
    </div>
  </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    const Login = () => import(/* webpackChunkName: "login" */ '@/components/auth/Login.vue');
    const Register = () => import(/* webpackChunkName: "register" */ '@/components/auth/Register.vue');

    @Component({
        components: {
            Login,
            Register,
        },
    })
    export default class Auth extends Vue {

        // ========== Data ========== //


        // ========== Computed ========== //

        public get routeName(): string {
            return this.$route.name || '';
        }


        // ========== Lifecycle hooks ========== //


        // ========== Methods ========== //

        public onLogin() {
            const next = this.$route.query.next;
            if (typeof next === 'string') {
                this.$router.push(next);
            } else {
                this.$router.push({name: 'home'});
            }
        }

        public onRegister() {
            this.$router.push({name: 'login'});
        }


        // ========== Watchers ========== //

    }
</script>

<style lang="scss" scoped>
  .container {
    @media screen and (min-width: 769px) {
      width: 350px;
      margin-top: 5rem;
    }
  }
</style>
