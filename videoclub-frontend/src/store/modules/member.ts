/*
 * Copyright 2019 Juan José González Abril
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

import {getModule, Module, MutationAction, VuexModule} from 'vuex-module-decorators';
import store from '@/store';
import {getMemberData} from '@/api/member';
import {AuthModule} from '@/store/modules/auth';
import {converter} from '@/util/JsonConverter';
import {Member} from '@/data/Member';

interface IMemberState {
    data: Member | null;
}

@Module({dynamic: true, store, name: 'member'})
class MemberStore extends VuexModule implements IMemberState {
    public data: Member | null = null;

    @MutationAction({mutate: ['data']})
    public async fetchMemberData(): Promise<IMemberState> {
        if (AuthModule.isAuthenticated) {
            const response = await getMemberData();
            const data = converter.deserializeObject(response.data, Member);
            return {data};
        } else {
            return {data: null};
        }
    }
}

export const MemberModule = getModule(MemberStore);
