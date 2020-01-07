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
      <StatList v-if="routeName === 'stat-list'" />
      <StatViewer v-else-if="routeName === 'stat-view'" />
      <StatEditor v-else-if="routeName === 'stat-create'" />
    </transition>
  </section>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    const StatList = () => import(/* webpackChunkName: "stat_list" */ '@/components/list/StatList.vue');
    const StatViewer = () => import(/* webpackChunkName: "stat_viewer" */ '@/components/viewer/StatViewer.vue');
    const StatEditor = () => import(/* webpackChunkName: "stat_editor" */ '@/components/editor/StatEditor.vue');

    @Component({
        components: {
            StatList,
            StatViewer,
            StatEditor,
        },
    })
    export default class Stats extends Vue {

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
