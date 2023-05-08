import {OutgoingEventDescription} from "@/features/outgoingevents/outgoing-event-description";

export class OutgoingEventCreateService {
    constructor(readonly backendUrl: string) {
    }

    getAllTypes(): Promise<OutgoingEventType[]> {
        return fetch(`${this.backendUrl}/outgoing-events/types`)
            .then(response => response.json());
    }

    getNewOutgoingEventDescription(type: OutgoingEventType): Promise<OutgoingEventDescription> {
        return fetch(`${this.backendUrl}/outgoing-events/types/${type}`)
            .then(response => response.json())
            .then(json => OutgoingEventDescription.fromJson(json));
    }
}

export type OutgoingEventType = string