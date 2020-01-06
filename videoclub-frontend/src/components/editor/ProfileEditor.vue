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
        <form
          action=""
          method="post">
          <header>
            <h1 class="title">
              Profile editor
            </h1>
          </header>
          <hr>
          <section>
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
                v-model="data.name"
                :loading="isLoading"
                required />
            </b-field>

            <b-field v-if="isMember">
              <template slot="label">
                Age
              </template>
              <b-numberinput
                v-model="data.age"
                :loading="isLoading"
                min="1"
                max="150"
                required />
            </b-field>
          </section>
          <hr>
          <footer class="buttons is-right">
            <b-button
              type="is-success"
              @click="onSave">
              Save
            </b-button>
            <b-button
              type="is-danger"
              @click="onCancel">
              Cancel
            </b-button>
          </footer>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import cloneDeep from 'lodash.clonedeep';
    import {Admin} from '@/data/Admin';
    import {UserModule} from '@/store/modules/user';
    import {Member} from '@/data/Member';
    import {patchAdmin, patchMember} from '@/api/modules/user';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BInput = () => import(/* webpackChunkName: "b_input" */ 'buefy/src/components/input/Input.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');
    const BNumberinput = () => import(/* webpackChunkName: "b_numberinput" */ 'buefy/src/components/numberinput/Numberinput.vue');

    @Component({
        components: {
            BField,
            BInput,
            BButton,
            BNumberinput,
        },
    })
    export default class ProfileEditor extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: Member | Admin = null!;
        public isLoading: boolean = true;


        // ========== Computed ========== //

        public get isMember(): boolean {
            return UserModule.isMember;
        }


        public get username(): string {
            return UserModule.user?.username || '';
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            if (UserModule.isAdmin) {
                this.data = cloneDeep(UserModule.admin!!);
            } else {
                this.data = cloneDeep(UserModule.member!!);
            }

            this.isLoading = false;
        }


        // ========== Methods ========== //

        public onCancel() {
            this.$buefy.dialog.confirm({
                title: 'Close editor',
                message: 'Are you sure you want close the editor?<br>Any unsaved data will be lost.',
                cancelText: 'Keep editing',
                confirmText: 'Close editor',
                type: 'is-danger',
                hasIcon: true,
                onConfirm: () => this.$router.push({name: 'profile-view'}),
            });
        }

        public async onSave() {
            if (this.data instanceof Admin) {
                await patchAdmin(this.data);
            } else {
                await patchMember(this.data);
            }
            await UserModule.load();
            await this.$router.push({name: 'profile-view'});
        }


        // ========== Watchers ========== //
    }
</script>

<style scoped>

</style>
