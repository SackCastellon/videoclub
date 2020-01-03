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

import {JsonObject, JsonProperty} from 'json2typescript';

@JsonObject
export class Shop {
    @JsonProperty('id', Number)
    public id: number = undefined!;

    @JsonProperty('manager', String)
    public manager: string = undefined!;

    @JsonProperty('city', String)
    public city: string = undefined!;

    @JsonProperty('street', String)
    public street: string = undefined!;

    @JsonProperty('zipCode', String)
    public zipCode: string = undefined!;
}
