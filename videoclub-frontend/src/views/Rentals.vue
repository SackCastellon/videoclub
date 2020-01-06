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
    <transition
      mode="out-in"
      name="fade">
      <RentalList v-if="routeName === 'rental-list'" />
      <RentalViewer v-else-if="routeName === 'rental-view'" />
      <RentalEditor v-else-if="routeName === 'rental-edit'" />
    </transition>
  </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    const RentalList = () => import(/* webpackChunkName: "rental_list" */ '@/components/list/RentalList.vue');
    const RentalViewer = () => import(/* webpackChunkName: "rental_viewer" */ '@/components/viewer/RentalViewer.vue');
    const RentalEditor = () => import(/* webpackChunkName: "rental_editor" */ '@/components/editor/RentalEditor.vue');

    @Component({
        components: {
            RentalList,
            RentalViewer,
            RentalEditor,
        },
    })
    export default class Rentals extends Vue {

        // ========== Props ========== //


        // ========== Data ========== //


        // ========== Computed ========== //

        public get routeName(): string {
            return this.$route.name || '';
        }


        // ========== Lifecycle Hooks ========== //


        // ========== Methods ========== //


        // ========== Watchers ========== //
    };
</script>
