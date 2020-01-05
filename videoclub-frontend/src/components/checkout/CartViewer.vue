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
  <div>
    <h2
      v-if="size"
      class="subtitle">
      You have added a total of {{ size }} movies to the cart.
    </h2>
    <b-table
      :data="content"
      :striped="true"
      :loading="isLoading">
      <template v-slot:default="props">
        <b-table-column
          field="id"
          label="ID"
          width="40"
          numeric>
          {{ props.row.id }}
        </b-table-column>

        <b-table-column
          field="shipId"
          label="Shop"
          width="40"
          numeric>
          {{ props.row.shopId }}
        </b-table-column>

        <b-table-column
          field="name"
          label="Title">
          <router-link :to="{name: 'movie-view', params: {id: props.row.id}}">
            {{ props.row.name }}
          </router-link>
        </b-table-column>

        <b-table-column
          field="director"
          label="Director">
          {{ props.row.director }}
        </b-table-column>

        <b-table-column
          field="releaseDate"
          label="Release date"
          centered>
          {{ formatDate(props.row.releaseDate) }}
        </b-table-column>

        <b-table-column
          field="price"
          label="Price"
          centered>
          {{ `${props.row.price.toFixed(2)} €` }}
        </b-table-column>

        <b-table-column
          label=""
          centered>
          <b-tooltip label="Remove from cart">
            <a @click="onRemove(props.row)">
              <b-icon
                icon="cart-arrow-up"
                type="is-danger" />
            </a>
          </b-tooltip>
        </b-table-column>
      </template>
      <template
        v-if="!isEmpty"
        v-slot:footer>
        <th class="is-hidden-mobile" />
        <th class="is-hidden-mobile" />
        <th class="is-hidden-mobile" />
        <th class="is-hidden-mobile" />
        <th class="is-hidden-mobile">
          <div class="th-wrap is-numeric">
            Total:
          </div>
        </th>
        <th class="is-hidden-mobile">
          <div class="th-wrap is-centered">
            {{ totalPrice }}
          </div>
        </th>
        <th class="is-hidden-mobile" />
      </template>
      <template v-slot:empty>
        <section class="section">
          <div class="content has-text-grey has-text-centered">
            <p>
              <b-icon
                icon="cart-outline"
                size="is-large" />
            </p>
            <p>Looks like there is nothing in the cart.</p>
          </div>
        </section>
      </template>
    </b-table>
    <div class="is-hidden-tablet">
      <div class="d-flex justify-content-between">
        <p>
          Total
        </p>
        <p>
          {{ totalPrice }}
        </p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import {CartModule} from '@/store/modules/cart';
    import {Movie} from '@/data/Movie';
    import {dateFormat} from '@/util/Moment';
    import moment from 'moment';

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
    export default class CartViewer extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public isLoading = true;


        // ========== Computed ========== //

        public get isEmpty(): boolean {
            return CartModule.isEmpty;
        }

        public get size(): number {
            return CartModule.count;
        }

        public get content(): ReadonlyArray<Movie> {
            return CartModule.movies;
        }

        public get totalPrice(): string {
            return CartModule.totalPrice.toFixed(2) + ' €';
        }


        // ========== Lifecycle Hooks ========== //

        // noinspection JSUnusedGlobalSymbols
        public async created() {
            await CartModule.load();
            this.isLoading = false;
        }


        // ========== Methods ========== //

        public formatDate(date: Date): string {
            return moment(date).format(dateFormat);
        }

        public onRemove(movie: Movie) {
            CartModule.remove(movie);
        }


        // ========== Watchers ========== //
    }
</script>

<style scoped lang="scss">
  div.b-table + div > div.d-flex {
    margin-top: 1rem;
    padding: .5em .75em;
    border-bottom: 1px solid #f5f5f5;
    box-shadow: 0 2px 3px rgba(10, 10, 10, .1), 0 0 0 1px rgba(10, 10, 10, .1);

    p {
      font-weight: 600;
    }
  }
</style>
