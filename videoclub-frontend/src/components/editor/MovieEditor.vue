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
              {{ isEdit ? 'Editing: ' : 'New movie' }}
              <i
                v-if="isEdit && !isLoading"
                class="has-text-grey">{{ `Movie #${data.id}` }}</i>
            </h1>
          </header>
          <hr>
          <section>
            <b-field>
              <template slot="label">
                Title
              </template>
              <b-input
                v-model="data.name"
                :loading="isLoading"
                icon="movie"
                required />
            </b-field>

            <b-field>
              <template slot="label">
                Director
              </template>
              <b-input
                v-model="data.director"
                :loading="isLoading"
                icon="account-tie"
                required />
            </b-field>

            <b-field>
              <template slot="label">
                Release date
              </template>
              <b-datepicker
                v-model="data.releaseDate"
                :loading="isLoading"
                icon="calendar-today"
                required />
            </b-field>

            <ShopInput
              v-model="data.shopId"
              title="Shop"
              :loading="isLoading"
              :readonly="isEdit" />

            <b-field>
              <template slot="label">
                Price
              </template>
              <b-numberinput
                v-model="data.price"
                :min="0.1"
                :step="0.1"
                :max="100"
                :loading="isLoading"
                icon="currency-eur"
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
    import {Movie} from '@/data/Movie';
    import cloneDeep from 'lodash.clonedeep';
    import {MovieModule} from '@/store/modules/movies';
    import {patchMovie, postMovie} from '@/api/modules/movies';

    const BField = () => import(/* webpackChunkName: "b_field" */ 'buefy/src/components/field/Field.vue');
    const BInput = () => import(/* webpackChunkName: "b_input" */ 'buefy/src/components/input/Input.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');
    const BDatepicker = () => import(/* webpackChunkName: "b_datepicker" */ 'buefy/src/components/datepicker/Datepicker.vue');
    const BNumberinput = () => import(/* webpackChunkName: "b_numberinput" */ 'buefy/src/components/numberinput/Numberinput.vue');
    const ShopInput = () => import(/* webpackChunkName: "shop_input" */ '@/components/input/ShopInput.vue');

    @Component({
        components: {
            ShopInput,
            BField,
            BInput,
            BButton,
            BDatepicker,
            BNumberinput,
        },
    })
    export default class MovieEditor extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public data: Movie = new Movie();
        public isLoading: boolean = true;


        // ========== Computed ========== //

        public get isEdit(): boolean {
            return this.$route.name === 'movie-edit';
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            if (this.isEdit) {
                const id = parseInt(this.$route.params['id']);
                if (isNaN(id)) {
                    this.$buefy.toast.open({message: 'Invalid movie ID', type: 'is-warning'});
                    return this.$router.push({name: 'movie-list'});
                }

                const movie = await MovieModule.get(id);

                if (movie) this.data = cloneDeep(movie);
                else {
                    this.$buefy.toast.open({message: `No movie with ID ${id} was found`, type: 'is-warning'});
                    return this.$router.push({name: 'movie-list'});
                }
            } else {
                this.data.price = 0.1;
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
                        this.$router.push({name: 'movie-view', params: {id: this.data.id.toString()}});
                    } else {
                        this.$router.push({name: 'movie-list'});
                    }
                },
            });
        }

        public async onSave() {
            if (this.isEdit) {
                await patchMovie(this.data.id, this.data);
                await this.$router.push({name: 'movie-view', params: {id: this.data.id.toString()}});
            } else {
                const {data} = await postMovie(this.data);
                await this.$router.push({name: 'movie-view', params: {id: data.movieId.toString()}});
            }
        }


        // ========== Watchers ========== //
    }
</script>

