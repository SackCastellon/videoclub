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
  <div class="columns is-centered">
    <div class="column is-three-quarters-tablet is-half-desktop">
      <h1 class="title">
        Profile
      </h1>
      <hr>
      <b-field horizontal>
        <template slot="label">
          Username
        </template>
        <b-input
          :value="username"
          :loading="isLoading"
          readonly />
      </b-field>
      <b-field horizontal>
        <template slot="label">
          Name
        </template>
        <b-input
          :value="name"
          :loading="isLoading"
          readonly />
      </b-field>
      <b-field horizontal>
        <template slot="label">
          Age
        </template>
        <b-input
          :value="age"
          :loading="isLoading"
          readonly />
      </b-field>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {MemberModule} from '@/store/modules/member';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BInput = () => import(/* webpackChunkName: "b_input" */ 'buefy/src/components/input/Input.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');
    const BLoading = () => import(/* webpackChunkName: "b_loading" */ 'buefy/src/components/loading/Loading.vue');

    @Component({
        components: {
            BField,
            BInput,
            BButton,
            BLoading,
        },
    })
    export default class ProfileViewer extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //


        // ========== Computed ========== //

        public get name(): string {
            return MemberModule.name;
        }

        public get age(): number {
            return MemberModule.age;
        }

        public get username(): string {
            return MemberModule.username;
        }

        public get isLoading(): boolean {
            return !MemberModule.isLoaded;
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public created() {
            if (!MemberModule.isLoaded) MemberModule.fetchMemberData();
        }


        // ========== Methods ========== //


        // ========== Watchers ========== //
    }
</script>
