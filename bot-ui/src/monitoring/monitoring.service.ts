export class MonitoringService {
    constructor(
        private readonly backendUrl: string) {

    }

    getStatistics(): Promise<MonitoringEvent[]> {
        return fetch(`${this.backendUrl}/monitoring/events`)
            .then(response => response.json())
    }
}

export interface MonitoringEvent {
    date: Date,
    event: EventPayload
}

export interface EventPayload {
    type: string,
    event: IncomingEventPayload
}

export interface IncomingEventPayload {
    type: string
}