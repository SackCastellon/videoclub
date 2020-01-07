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
    <h1 class="title is-1 is-spaced">
      Stats
    </h1>
    <h5 class="subtitle is-5">
      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas lacus dolor, lacinia in commodo ornare,
      condimentum ut ligula. Mauris sed mauris consequat, condimentum nisl id, porta tortor. In consectetur vulputate
      est, et sollicitudin magna tempus a. Vivamus eu ultrices velit. In gravida fermentum porttitor.
    </h5>
    <!--<div class="columns is-centered">
      <div class="column is-10">
        <b-field>
          <b-input
            placeholder="Search"
            type="search"
            icon="magnify" />
          <p class="control">
            <button class="button is-primary">
              Search
            </button>
          </p>
        </b-field>
      </div>
    </div>-->
    <div class="buttons is-right">
      <b-button
        tag="router-link"
        :to="{name: 'stat-create'}"
        type="is-primary"
        outlined>
        New stat
      </b-button>
    </div>
    <hr class="mt-0">
    <b-table
      :data="data"
      :striped="true"
      :loading="isLoading">
      <template slot-scope="props">
        <b-table-column
          field="id"
          label="ID"
          width="40"
          numeric>
          {{ props.row.id }}
        </b-table-column>

        <b-table-column
          field="memberId"
          label="Member">
          {{ memberName(props.row.memberId) }}
        </b-table-column>

        <b-table-column
          field="creationDate"
          label="Creation date">
          {{ formatDate(props.row.creationDate) }}
        </b-table-column>

        <b-table-column
          field="totalSpent"
          label="Total spent">
          {{ props.row.totalSpent.toFixed(2) + ' €' }}
        </b-table-column>

        <b-table-column
          label="">
          <div class="buttons is-centered">
            <b-button
              tag="router-link"
              type="is-info"
              :to="{name:'stat-view', params: {id: props.row.id}}">
              View stat
            </b-button>
          </div>
        </b-table-column>
      </template>
    </b-table>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {StatModule} from '@/store/modules/stats';
    import {Stat} from '@/data/Stat';
    import {User} from '@/data/User';
    import {getUsers} from '@/api/modules/user';
    import {converter} from '@/util/JsonConverter';
    import {moment} from '@/util/Moment';

    const BTable = () => import(/* webpackChunkName: "b_table" */ 'buefy/src/components/table/Table.vue');
    const BTableColumn = () => import(/* webpackChunkName: "b_table_column" */ 'buefy/src/components/table/TableColumn.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');

    @Component({
        components: {
            BTable,
            BTableColumn,
            BButton,
        },
    })
    export default class StatList extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: ReadonlyArray<Stat> = [];
        public users: ReadonlyArray<User> = [];
        public isLoading = true;


        // ========== Computed ========== //

        public get memberName(): (id: number) => string {
            return (id) => this.users.find(it => it.id === id)?.username || '';
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            await StatModule.load();
            this.data = StatModule.stats;
            this.users = converter.deserializeArray((await getUsers()).data, User);
            this.isLoading = false;
        }


        // ========== Methods ========== //

        public formatDate(date: Date) {
            return moment(date).format('MM/YYYY');
        }


        // ========== Watchers ========== //
    };
</script>

<style scoped lang="scss">
  img {
    width: 300px;
    max-width: 100%;
  }
</style>
