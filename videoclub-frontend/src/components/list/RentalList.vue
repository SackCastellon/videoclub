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
      Rentals
    </h1>
    <h5 class="subtitle is-5">
      Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi at mi nisl. Interdum et malesuada fames ac ante
      ipsum primis in faucibus. Quisque dapibus, sem nec hendrerit consectetur, mauris lorem porta lectus, nec interdum
      libero diam sed dui. Nunc quis diam placerat, venenatis urna quis, pretium magna.
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
          field="city"
          label="City">
          {{ formatDate(props.row.pickupDate) }}
        </b-table-column>
        <b-table-column
          field="street"
          label="Street">
          {{ formatDate(props.row.returnDate) }}
        </b-table-column>
        <b-table-column
          label="">
          <div class="buttons is-centered">
            <b-button
              tag="router-link"
              type="is-info"
              :to="{name:'rental-view', params: {id: props.row.id}}">
              View rental
            </b-button>
            <b-button
              tag="router-link"
              :to="{name:'rental-edit', params: {id: props.row.id}}">
              Edit rental
            </b-button>
          </div>
        </b-table-column>
      </template>
    </b-table>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {RentalModule} from '@/store/modules/rentals';
    import {Rental} from '@/data/Rental';
    import {dateFormat, moment} from '@/util/Moment';

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
    export default class RentalList extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: ReadonlyArray<Rental> = [];
        public isLoading = true;


        // ========== Computed ========== //


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            await RentalModule.load();
            this.data = RentalModule.rentals;
            this.isLoading = false;
        }


        // ========== Methods ========== //

        public formatDate(date: Date | undefined): string {
            return date ? moment(date).format(dateFormat) : '';
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
