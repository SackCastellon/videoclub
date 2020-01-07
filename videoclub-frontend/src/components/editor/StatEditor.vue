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
              New stat
            </h1>
          </header>
          <hr>
          <section>
            <UserInput
              v-model="data.memberId"
              title="User" />

            <b-field>
              <template slot="label">
                Creation date
              </template>
              <b-datepicker
                v-model="data.creationDate"
                type="month"
                :date-formatter="dateFormatter"
                :date-parser="dateParser"
                icon="calendar"
                editable
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
    import {StatNew} from '@/data/Stat';
    import {postStat} from '@/api/modules/stats';
    import moment from 'moment';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BDatepicker = () => import(/* webpackChunkName: "b_datepicker" */ 'buefy/src/components/datepicker/Datepicker.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');

    const UserInput = () => import(/* webpackChunkName: "user_input" */ '@/components/input/UserInput.vue');

    @Component({
        components: {
            BField,
            BDatepicker,
            BButton,
            UserInput,
        },
    })
    export default class StatEditor extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: StatNew = new StatNew();
        public isLoading: boolean = true;


        // ========== Computed ========== //


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //

        public onCancel() {
            this.$buefy.dialog.confirm({
                title: 'Close editor',
                message: 'Are you sure you want close the editor?<br>Any unsaved data will be lost.',
                cancelText: 'Keep editing',
                confirmText: 'Close editor',
                type: 'is-danger',
                hasIcon: true,
                onConfirm: () => this.$router.push({name: 'stat-list'}),
            });
        }

        public async onSave() {
            const {data} = await postStat(this.data);
            await this.$router.push({name: 'stat-view', params: {id: data.statId.toString()}});
        }

        public dateFormatter(date: Date): string {
            return moment(date).format('MM/YYYY');
        }

        public dateParser(s: string): Date {
            return moment(s, 'MM/YYYY').toDate();
        }


        // ========== Watchers ========== //
    }
</script>

<style scoped>

</style>
