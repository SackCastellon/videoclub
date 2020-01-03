<!--
  - Copyright 2019 Juan José González Abril
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
      <ShopList v-if="routeName === 'shop-list'" />
      <ShopViewer v-else-if="routeName === 'shop-view'" />
      <ShopEditor v-else-if="routeName === 'shop-edit' || routeName === 'shop-create'" />
    </transition>
  </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    const ShopList = () => import(/* webpackChunkName: "shop_list" */ '@/components/list/ShopList.vue');
    const ShopViewer = () => import(/* webpackChunkName: "shop_viewer" */ '@/components/viewer/ShopViewer.vue');
    const ShopEditor = () => import(/* webpackChunkName: "shop_editor" */ '@/components/editor/ShopEditor.vue');

    @Component({
        components: {
            ShopList,
            ShopViewer,
            ShopEditor,
        },
    })
    export default class Shops extends Vue {

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

<style scoped lang="scss">
</style>
