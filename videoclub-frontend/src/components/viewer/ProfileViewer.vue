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
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-9-tablet is-8-desktop is-7-widescreen is-6-fullhd">
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

        <b-field v-if="isMember">
          <template slot="label">
            Age
          </template>
          <b-input
            :value="age"
            :loading="isLoading"
            readonly />
        </b-field>
        <hr>
        <div class="buttons is-right">
          <b-button
            type="is-primary"
            outlined
            :loading="isLoading"
            @click="onEdit">
            Edit information
          </b-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {UserModule} from '@/store/modules/user';

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

        public isLoading: boolean = true;


        // ========== Computed ========== //

        public get isAdmin(): boolean {
            return UserModule.isAdmin;
        }

        public get isMember(): boolean {
            return UserModule.isMember;
        }


        public get username(): string {
            return UserModule.user?.username || '';
        }

        public get name(): string {
            if (UserModule.isAdmin) {
                return UserModule.admin?.name || '';
            } else {
                return UserModule.member?.name || '';
            }
        }

        public get age(): string {
            if (UserModule.isAdmin) {
                return '';
            } else {
                return UserModule.member?.age?.toString() || '';
            }
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            if (!UserModule.isLoaded) await UserModule.load();

            this.isLoading = false;
        }


        // ========== Methods ========== //

        public onEdit() {
            this.$router.push({name: 'profile-edit'});
        }


        // ========== Watchers ========== //
    }
</script>
