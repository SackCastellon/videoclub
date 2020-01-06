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
      <div class="column is-10-desktop is-9-widescreen is-8-fullhd">
        <router-link
          class="button mb-3"
          :to="{name: 'rental-list'}">
          <b-icon
            icon="chevron-left"
            class="mr-1" />
          <p>Back to list</p>
        </router-link>
        <h1
          class="title"
          :class="{'is-italic has-text-grey-light': isLoading}">
          {{ isLoading ? 'Rental ##' : `Rental #${data.id}` }}
        </h1>
        <hr>
        <div class="columns">
          <div class="column is-two-fifths">
            <b-field>
              <template slot="label">
                Number of movies
              </template>
              <b-input
                :value="movies.length"
                icon="numeric"
                readonly />
            </b-field>

            <b-field>
              <template slot="label">
                Total cost
              </template>
              <b-input
                :value="rentalCost"
                :loading="isLoading"
                icon="currency-eur"
                readonly />
            </b-field>

            <b-field>
              <template slot="label">
                Pickup date
              </template>
              <b-input
                :value="rentalPickupDate"
                :loading="isLoading"
                icon="calendar"
                readonly />
            </b-field>

            <b-field>
              <template slot="label">
                Return date
              </template>
              <b-input
                :value="rentalReturnDate"
                :loading="isLoading"
                icon="calendar"
                readonly />
            </b-field>
          </div>
          <div class="column is-three-fifths">
            <b-table
              :data="movies"
              :striped="true"
              :mobile-cards="false">
              <template v-slot:default="props">
                <b-table-column
                  field="name"
                  label="Title">
                  {{ props.row.name }}
                </b-table-column>

                <b-table-column
                  field="price"
                  label="Price"
                  centered>
                  {{ `${props.row.price.toFixed(2)} €` }}
                </b-table-column>
              </template>
            </b-table>
          </div>
        </div>
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
    import {Rental} from '@/data/Rental';
    import {RentalModule} from '@/store/modules/rentals';
    import {dateFormat, moment} from '@/util/Moment';
    import {Movie} from '@/data/Movie';
    import {getRentalMovies} from '@/api/modules/movies';
    import {converter} from '@/util/JsonConverter';

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
    export default class RentalViewer extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: Rental | null = null;
        public movies: ReadonlyArray<Movie> = [];


        // ========== Computed ========== //

        public get rentalPickupDate(): string {
            if (this.data) {
                return moment(this.data.pickupDate).format(dateFormat);
            } else {
                return '';
            }
        }

        public get rentalReturnDate(): string {
            if (this.data) {
                return moment(this.data.returnDate).format(dateFormat);
            } else {
                return '';
            }
        }

        public get rentalCost(): string {
            return this.data?.cost.toFixed(2) + ' €' || '';
        }

        public get isLoading(): boolean {
            return this.data === null || this.movies.length === 0;
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            const id = parseInt(this.$route.params['id']);
            if (isNaN(id)) {
                this.$buefy.toast.open({message: 'Invalid rental ID', type: 'is-warning'});
                return this.$router.push({name: 'rental-list'});
            }

            const rental = await RentalModule.get(id);
            if (rental) {
                this.data = rental;
                this.movies = converter.deserializeArray((await getRentalMovies(rental.id)).data, Movie);
            } else {
                this.$buefy.toast.open({message: `No rental with ID ${id} was found`, type: 'is-warning'});
                return this.$router.push({name: 'rental-list'});
            }
        }


        // ========== Methods ========== //

        public onEdit() {
            const id = this.data?.id;
            if (id !== undefined) {
                this.$router.push({name: 'rental-edit', params: {id: id.toString()}});
            }
        }


        // ========== Watchers ========== //
    }
</script>
