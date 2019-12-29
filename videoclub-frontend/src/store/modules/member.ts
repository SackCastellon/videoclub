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
    member: Member | null;
}

@Module({dynamic: true, store, name: 'member'})
class MemberStore extends VuexModule implements IMemberState {
    public member: Member | null = null;

    public get name(): string {
        return this.member === null ? '' : this.member.name;
    }

    public get age(): number {
        return this.member === null ? -1 : this.member.age;
    }

    public get username(): string {
        return this.member === null ? '' : this.member.username;
    }

    public get isLoaded(): boolean {
        return this.member !== null;
    }

    @MutationAction({mutate: ['member']})
    public async fetchMemberData(): Promise<IMemberState> {
        if (AuthModule.isAuthenticated) {
            const response = await getMemberData();
            const data = converter.deserializeObject(response.data, Member);
            return {member: data};
        } else {
            return {member: null};
        }
    }
}

export const MemberModule = getModule(MemberStore);
