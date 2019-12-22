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
  <section class="section">
    <div class="container">
      <form
        method="post"
        @submit.prevent="onRegister(registrationInfo)">
        <div class="modal-card">
          <header class="modal-card-head">
            <p class="modal-card-title">
              Register
            </p>
          </header>
          <section class="modal-card-body">
            <b-field label="Name">
              <b-input
                v-model="registrationInfo.name"
                placeholder="Your full name"
                required />
            </b-field>

            <b-field label="Age">
              <b-numberinput
                v-model="registrationInfo.age"
                min="0"
                max="150"
                placeholder="Your age"
                required />
            </b-field>

            <b-field label="Username">
              <b-input
                v-model="registrationInfo.username"
                placeholder="Your username"
                required />
            </b-field>

            <b-field label="Password">
              <b-input
                v-model="registrationInfo.password"
                type="password"
                password-reveal
                placeholder="Your password"
                required />
            </b-field>
          </section>
          <footer class="modal-card-foot">
            <b-button
              tag="input"
              native-type="submit"
              type="is-primary"
              value="Register" />
          </footer>
        </div>
      </form>
    </div>
  </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {IRegistrationInfo} from '@/store/modules/auth';
    import {register} from '@/api/auth';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BInput = () => import(/* webpackChunkName: "b_input" */ 'buefy/src/components/input/Input.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');
    const BNumberInput = () => import(/* webpackChunkName: "b_number_input" */ 'buefy/src/components/numberinput/Numberinput.vue');

    @Component({
        components: {
            BField,
            BInput,
            BButton,
            BNumberInput,
        },
    })
    export default class Register extends Vue {
        protected registrationInfo: IRegistrationInfo = {
            name: '',
            age: 0,
            username: '',
            password: '',
        };

        protected async onRegister(registrationInfo: IRegistrationInfo) {
            const {status} = await register(registrationInfo);

            if (status == 200) {
                await this.$router.push({name: 'login'});
            }
        }
    }
</script>

<style lang="scss" scoped>
  .modal-card {
    @media screen and (min-width: 769px) {
      width: 350px;
      margin-top: 5rem;
    }
    margin: 0 auto;
    border: 1px solid #dbdbdb;
    border-radius: 6px;
    overflow: initial;
    max-height: initial;

    .modal-card-body {
      overflow: initial;
    }
  }
</style>
