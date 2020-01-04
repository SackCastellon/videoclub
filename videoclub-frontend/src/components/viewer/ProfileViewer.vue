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
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-10-tablet is-6-desktop">
        <h1 class="title">
          Profile
          <b-tooltip
            v-if="isAdmin"
            label="Admin">
            <b-icon
              icon="shield-check"
              size="is-medium"
              type="is-primary" />
          </b-tooltip>
        </h1>
        <hr>
        <b-field>
          <template slot="label">
            Username
          </template>
          <b-input
            :value="username"
            :loading="isLoading"
            readonly />
        </b-field>
        <b-field>
          <template slot="label">
            Name
          </template>
          <b-input
            :value="name"
            :loading="isLoading"
            readonly />
        </b-field>
        <b-field v-if="!isAdmin">
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
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {MemberModule} from '@/store/modules/member';
    import {UserModule} from '@/store/modules/user';
    import {UserType} from '@/data/User';
    import {AdminModule} from '@/store/modules/admin';

    const BTag = () => import(/* webpackChunkName: "b_tag" */ 'buefy/src/components/tag/Tag.vue');
    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BInput = () => import(/* webpackChunkName: "b_input" */ 'buefy/src/components/input/Input.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');

    @Component({
        components: {
            BTag,
            BField,
            BInput,
            BButton,
        },
    })
    export default class ProfileViewer extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //


        // ========== Computed ========== //

        public get name(): string {
            if (this.isAdmin) {
                return AdminModule.admin?.name || '';
            } else {
                return MemberModule.member?.name || '';
            }
        }

        public get age(): string | number {
            if (this.isAdmin) {
                return '';
            } else {
                const age = MemberModule.member?.age || -1;
                return age < 0 ? '' : age;
            }
        }

        public get username(): string {
            return UserModule.user?.username || '';
        }

        public get isLoading(): boolean {
            if (this.isAdmin) {
                return AdminModule.admin === null;
            } else {
                return MemberModule.member === null;
            }
        }

        public get isAdmin(): boolean {
            return UserModule.user?.type === UserType.ADMIN;
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public created() {
            if (this.isLoading) {
                if (this.isAdmin) {
                    AdminModule.load();
                } else {
                    MemberModule.load();
                }
            }
        }


        // ========== Methods ========== //


        // ========== Watchers ========== //
    }
</script>
