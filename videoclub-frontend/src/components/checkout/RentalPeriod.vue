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
  <div class="columns is-centered mb-2">
    <div class="column is-8-tablet is-6-desktop">
      <form
        action=""
        method="post">
        <h2 class="subtitle">
          Choose the renting period
        </h2>

        <b-field>
          <template slot="label">
            Pickup date
          </template>
          <b-datepicker
            v-model="data.pickupDate"
            icon="calendar-today"
            required />
        </b-field>

        <b-field>
          <template slot="label">
            Return date
          </template>
          <b-datepicker
            v-model="data.returnDate"
            :min-date="minDate"
            icon="calendar"
            required />
        </b-field>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import {RentalNew} from '@/data/Rental';
    import {moment} from '@/util/Moment';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BDatepicker = () => import(/* webpackChunkName: "b_datepicker" */ 'buefy/src/components/datepicker/Datepicker.vue');

    @Component({
        components: {
            BField,
            BDatepicker,
        },
    })
    export default class RentalPeriod extends Vue {

        // ========== Props ========== //

        @Prop({required: true})
        public data!: RentalNew;


        // ========== Data ========== //


        // ========== Computed ========== //

        public get minDate(): Date | null {
            // noinspection JSIncompatibleTypesComparison
            if (this.data.pickupDate !== undefined) {
                return moment(this.data.pickupDate).add(1, 'd').toDate();
            } else {
                return null;
            }
        }


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //


        // ========== Watchers ========== //
    }
</script>
