/*
 * Copyright 2020 Juan José González Abril
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {getModule, Module, Mutation, VuexModule} from 'vuex-module-decorators';
import store from '@/store';

@Module({
    dynamic: true,
    store,
    name: 'auth',
    namespaced: true,
})
class AuthStore extends VuexModule {

    public isAuthenticated: boolean = false;

    @Mutation
    public setAuthenticated(value: boolean) {
        this.isAuthenticated = value;
    }
}

export const AuthModule = getModule(AuthStore);
