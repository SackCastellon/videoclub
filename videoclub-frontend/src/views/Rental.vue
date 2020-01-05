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
  <section class="section">
    <div class="container">
      <div class="columns is-centered">
        <div class="column is-10-widescreen is-9-fullhd">
          <b-steps
            v-model="currentStep"
            size="is-medium"
            :animated="true">
            <b-step-item
              label="Cart"
              :icon="cartIcon"
              :clickable="false">
              <CartViewer />
            </b-step-item>

            <b-step-item
              label="Period"
              icon="calendar-multiple"
              :clickable="false">
              <RentalPeriod :data="rental" />
            </b-step-item>

            <b-step-item
              label="Account"
              :icon="userIcon"
              :clickable="false">
              <div class="columns my-2 is-centered">
                <div class="column is-5-desktop d-flex align-items-center justify-content-center">
                  <Login @success="onLoginSuccess" />
                </div>
                <div class="column is-5-desktop d-flex align-items-center justify-content-center">
                  <Register />
                </div>
              </div>
            </b-step-item>

            <b-step-item
              label="Summary"
              icon="format-list-numbered"
              :clickable="false">
              <h2 class="subtitle">
                Summary of your order
              </h2>
              <div class="columns mb-2">
                <div class="column is-half">
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
                      Total price
                    </template>
                    <b-input
                      :value="totalPrice"
                      icon="currency-eur"
                      readonly />
                  </b-field>

                  <b-field>
                    <template slot="label">
                      Pickup date
                    </template>
                    <b-input
                      :value="pickupDate"
                      icon="calendar-today"
                      readonly />
                  </b-field>

                  <b-field>
                    <template slot="label">
                      Return date
                    </template>
                    <b-input
                      :value="returnDate"
                      icon="calendar"
                      readonly />
                  </b-field>
                </div>
                <div class="column is-half">
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
            </b-step-item>

            <b-step-item
              label="Payment"
              icon="credit-card-outline"
              :clickable="false">
              <h1 class="title">
                Payment
              </h1>
            </b-step-item>

            <b-step-item
              label="Finish"
              icon="check"
              :clickable="false">
              <div class="d-flex justify-content-center">
                <b-message
                  type="is-success"
                  :has-icon="true"
                  class="shadow">
                  Your order has been placed.<br>
                  ID #{{ rentalId }}
                </b-message>
              </div>
              <div class="d-flex justify-content-center">
                <b-button
                  tag="router-link"
                  :to="{name:'home'}"
                  type="is-primary">
                  Return to home
                </b-button>
              </div>
            </b-step-item>

            <template v-slot:navigation="{previous, next}">
              <nav class="buttons is-centered">
                <button
                  v-if="!isFirst && !isLast"
                  class="button px-3"
                  :disabled="previous.disabled || !canGoPrev"
                  @click.prevent="onPrev(previous.action)">
                  <b-icon
                    icon="chevron-left"
                    class="mr-1" />
                  <p>{{ prevLabel }}</p>
                </button>
                <b-tooltip
                  v-if="!isLast"
                  size="is-small"
                  :label="tooltipText"
                  :active="hasTooltip"
                  multilined>
                  <button
                    class="button px-3 is-primary"
                    :disabled="next.disabled || !canGoNext"
                    @click.prevent="onNext(next.action)">
                    <p>{{ nextLabel }}</p>
                    <b-icon
                      icon="chevron-right"
                      class="ml-1" />
                  </button>
                </b-tooltip>
              </nav>
            </template>
          </b-steps>
        </div>
      </div>
    </div>
  </section>
</template>

<script lang="ts">
    import {Component, Vue, Watch} from 'vue-property-decorator';
    import {AuthModule} from '@/store/modules/auth';
    import {CartModule} from '@/store/modules/cart';
    import {RentalNew} from '@/data/Rental';
    import {UserModule} from '@/store/modules/user';
    import {UserType} from '@/data/User';
    import {Movie} from '@/data/Movie';
    import {postRental} from '@/api/rental';
    import {dateFormat, moment} from '@/util/Moment';

    const BSteps = () => import(/* webpackChunkName: "b_steps" */ 'buefy/src/components/steps/Steps.vue');
    const BStepItem = () => import(/* webpackChunkName: "b_step_item" */ 'buefy/src/components/steps/StepItem.vue');
    const BButton = () => import(/* webpackChunkName: "b_button" */ 'buefy/src/components/button/Button.vue');
    const BIcon = () => import(/* webpackChunkName: "b_icon" */ 'buefy/src/components/icon/Icon.vue');

    const CartViewer = () => import(/* webpackChunkName: "cart_viewer" */ '@/components/checkout/CartViewer.vue');
    const RentalPeriod = () => import(/* webpackChunkName: "rental_period" */ '@/components/checkout/RentalPeriod.vue');
    const Login = () => import(/* webpackChunkName: "login" */ '@/components/auth/Login.vue');
    const Register = () => import(/* webpackChunkName: "register" */ '@/components/auth/Register.vue');

    @Component({
        components: {
            BSteps,
            BStepItem,
            BButton,
            BIcon,
            CartViewer,
            RentalPeriod,
            Login,
            Register,
        },
    })
    export default class Checkout extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //

        public currentStep = 0;

        public rental: RentalNew = new RentalNew();
        public movies: ReadonlyArray<Movie> = [];
        public rentalId: number | null = null;

        // ========== Computed ========== //

        public get isAuthenticated(): boolean {
            return AuthModule.isAuthenticated;
        }

        public get isMember(): boolean {
            return UserModule.user?.type === UserType.MEMBER;
        }

        public get cartIcon(): string {
            return CartModule.isEmpty ? 'cart-outline' : 'cart';
        }

        public get userIcon(): string {
            return this.isAuthenticated ? 'account' : 'account-outline';
        }

        public get prevLabel(): string {
            switch (this.currentStep) {
                case 1:
                    return 'Cart';
                case 2:
                    return 'Period';
                case 3:
                    return this.isAuthenticated ? 'Period' : 'Account';
                case 4:
                    return 'Summary';
                default:
                    return '';
            }
        }

        public get nextLabel(): string {
            switch (this.currentStep) {
                case 0:
                    return 'Rent period';
                case 1:
                    return this.isAuthenticated && this.isMember ? 'Summary' : 'Account';
                case 2:
                    return 'Summary';
                case 3:
                    return 'Finish';
                case 4:
                    return 'Finish';
                default:
                    return '';
            }
        }

        public get hasTooltip(): boolean {
            return this.tooltipText.length > 0;
        }

        public get tooltipText(): string {
            if (!this.canGoNext) {
                switch (this.currentStep) {
                    case 0:
                        return 'Add at least one movie to the cart to continue';
                    case 1:
                        return 'Choose the pickup and return dates to continue';
                    case 2:
                        if (!this.isAuthenticated) {
                            return 'Login to your account to continue';
                        } else if (!this.isMember) {
                            return 'You need a member account to continue';
                        }
                }
            }

            return '';
        }

        public get canGoPrev(): boolean {
            return !this.isFirst && !this.isLast;
        }

        public get canGoNext(): boolean {
            switch (this.currentStep) {
                case 0:
                    return !CartModule.isEmpty;
                case 1:
                    return this.rental.pickupDate !== undefined && this.rental.returnDate !== undefined;
                case 2:
                    return this.isAuthenticated && this.isMember;
                default:
                    return !this.isLast;
            }
        }

        public get isFirst(): boolean {
            return this.currentStep === 0;
        }

        public get isLast(): boolean {
            return this.currentStep === 5;
        }

        public get totalPrice(): string {
            return this.movies.map(movie => movie.price).reduce(((a, b) => a + b), 0).toFixed(2) + ' €';
        }

        public get pickupDate(): string {
            return moment(this.rental.pickupDate).format(dateFormat);
        }

        public get returnDate(): string {
            return moment(this.rental.returnDate).format(dateFormat);
        }


        // ========== Lifecycle hooks ========== //


        // ========== Methods ========== //

        public onPrev(action: Function) {
            if (this.currentStep === 3 && this.isAuthenticated) {
                this.currentStep = 1;
            } else {
                action();
            }
        }

        public async onNext(action: Function) {
            switch (this.currentStep) {
                case 0:
                    this.movies = CartModule.movies;
                    this.rental.movieIds = this.movies.map(it => it.id);
                    break;
                case 1:
                    if (this.isAuthenticated) {
                        if (this.isMember) {
                            this.currentStep = 3;
                            return;
                        } else {
                            this.$nextTick(() => this.accountWarning());
                        }
                    }
                    break;
                case 3:
                    const {data} = await postRental(this.rental);
                    await CartModule.clear();
                    this.rentalId = data.rentalId;
                    this.currentStep = 5;
                    return;
            }

            action();
        }

        public onLoginSuccess() {
            if (this.isMember) {
                this.currentStep = 3;
            } else {
                this.accountWarning();
            }
        }

        private accountWarning() {
            this.$buefy.notification.open({
                type: 'is-warning',
                message: 'You need a member account to continue.<br>Please logout and login with a different account.',
                position: 'is-top',
                duration: 7500,
                hasIcon: true,
            });
        }


        // ========== Watchers ========== //

        @Watch('isAuthenticated')
        public onAuthentication(auth: boolean) {
            if (!auth && this.currentStep > 2) {
                this.currentStep = 2;
            }
        }
    }
</script>

<style lang="scss" scoped>
  article.message {
    @media screen and (min-width: 769px) {
      width: 50%;
    }
    margin-top: 5rem;
    margin-bottom: 5rem;
  }
</style>
