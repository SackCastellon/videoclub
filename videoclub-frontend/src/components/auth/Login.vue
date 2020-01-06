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
  <form
    method="post"
    @submit.prevent="onLogin">
    <div
      class="modal-card shadow"
      style="width: auto">
      <header class="modal-card-head">
        <p class="modal-card-title">
          Login
        </p>
      </header>
      <section class="modal-card-body">
        <b-field label="Username">
          <b-input
            v-model="info.username"
            v-focus
            placeholder="Your username"
            required />
        </b-field>

        <b-field label="Password">
          <b-input
            v-model="info.password"
            type="password"
            password-reveal
            placeholder="Your password"
            required />
        </b-field>
      </section>
      <footer class="modal-card-foot">
        <div class="buttons is-centered">
          <b-button
            tag="input"
            native-type="submit"
            type="is-primary"
            value="Login" />
        </div>
      </footer>
    </div>
  </form>
</template>
<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {ILoginInfo, login} from '@/api/modules/auth';
    import {UserModule} from '@/store/modules/user';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BInput = () => import(/* webpackChunkName: "b_input" */ 'buefy/src/components/input/Input.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');

    @Component({
        components: {
            BField,
            BInput,
            BButton,
        },
    })
    export default class Login extends Vue {

        // ========== Data ========== //

        protected info: ILoginInfo = {
            username: '',
            password: '',
        };


        // ========== Computed ========== //


        // ========== Lifecycle hooks ========== //


        // ========== Methods ========== //

        public async onLogin() {
            try {
                await login(this.info);
                this.clear();
                await UserModule.load();
                this.$buefy.toast.open({
                    type: 'is-success',
                    message: 'Signed in successfully',
                });
                this.$emit('success');
            } catch (error) {
                // TODO Improve error message
                this.$buefy.toast.open({
                    type: 'is-danger',
                    message: 'Something went wrong during the sign in process',
                });
            }
        }

        public clear() {
            this.info.username = '';
            this.info.password = '';
        }


        // ========== Watchers ========== //

    }
</script>

<style lang="scss" scoped>
  .modal-card {
    margin: 0 auto;
    border: 1px solid #dbdbdb;
    border-radius: 6px;
    overflow: initial;
    max-height: initial;

    .modal-card-foot .buttons {
      width: 100%;
    }
  }
</style>
