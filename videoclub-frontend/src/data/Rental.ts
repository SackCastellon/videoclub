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
import {DateConverter} from '@/data/serializaion/DateConverter';

@JsonObject
export class Rental {
    @JsonProperty('id', Number)
    public id: number = undefined!;

    @JsonProperty('memberId', Number)
    public memberId: number = undefined!;

    @JsonProperty('movieIds', [Number])
    public movieIds: number[] = undefined!;

    @JsonProperty('pickupDate', DateConverter)
    public pickupDate: Date = undefined!;

    @JsonProperty('returnDate', DateConverter)
    public returnDate: Date = undefined!;

    @JsonProperty('cost', Number)
    public cost: number = undefined!;
}

@JsonObject
export class RentalNew {
    @JsonProperty('movieIds', [Number])
    public movieIds: number[] = undefined!;

    @JsonProperty('pickupDate', DateConverter)
    public pickupDate: Date = undefined!;

    @JsonProperty('returnDate', DateConverter)
    public returnDate: Date = undefined!;
}

@JsonObject
export class RentalUpdate {
    @JsonProperty('returnDate', DateConverter)
    public returnDate: Date = undefined!;
}
