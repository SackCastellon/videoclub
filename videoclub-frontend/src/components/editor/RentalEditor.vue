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
        <form
          action=""
          method="post">
          <header>
            <h1 class="title">
              Editing:
              <i class="has-text-grey">{{ `Rental #${data.id}` }}</i>
            </h1>
          </header>
          <hr>
          <section>
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
                    :value="data.cost"
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
                  <b-datepicker
                    v-model="data.returnDate"
                    :min-date="data.pickupDate"
                    :loading="isLoading"
                    icon="calendar-today"
                    editable
                    required />
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
    import {Rental} from '@/data/Rental';
    import cloneDeep from 'lodash.clonedeep';
    import {RentalModule} from '@/store/modules/rentals';
    import {patchRental, postRental} from '@/api/modules/rentals';
    import {Movie} from '@/data/Movie';
    import {converter} from '@/util/JsonConverter';
    import {getRentalMovies} from '@/api/modules/movies';
    import {dateFormat, moment} from '@/util/Moment';

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
    export default class RentalEditor extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: Rental = new Rental();
        public movies: ReadonlyArray<Movie> = [];
        public isLoading: boolean = true;


        // ========== Computed ========== //

        public get isEdit(): boolean {
            return this.$route.name === 'rental-edit';
        }

        public get rentalPickupDate(): string {
            return moment(this.data.pickupDate).format(dateFormat);
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            if (this.isEdit) {
                const id = parseInt(this.$route.params['id']);
                if (isNaN(id)) {
                    this.$buefy.toast.open({message: 'Invalid rental ID', type: 'is-warning'});
                    return this.$router.push({name: 'rental-list'});
                }

                const rental = await RentalModule.get(id);

                if (rental) {
                    this.data = cloneDeep(rental);
                    this.movies = converter.deserializeArray((await getRentalMovies(rental.id)).data, Movie);
                } else {
                    this.$buefy.toast.open({message: `No rental with ID ${id} was found`, type: 'is-warning'});
                    return this.$router.push({name: 'rental-list'});
                }
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
                onConfirm: () => {
                    if (this.isEdit) {
                        this.$router.push({name: 'rental-view', params: {id: this.data.id.toString()}});
                    } else {
                        this.$router.push({name: 'rental-list'});
                    }
                },
            });
        }

        public async onSave() {
            if (this.isEdit) {
                await patchRental(this.data.id, this.data);
                await RentalModule.load();
                await this.$router.push({name: 'rental-view', params: {id: this.data.id.toString()}});
            } else {
                const {data} = await postRental(this.data);
                await this.$router.push({name: 'rental-view', params: {id: data.rentalId.toString()}});
            }
        }


        // ========== Watchers ========== //
    }
</script>

<style scoped>

</style>
