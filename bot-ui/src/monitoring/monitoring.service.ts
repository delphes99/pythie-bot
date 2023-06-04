export class MonitoringService {
    constructor(
        private readonly backendUrl: string) {

    }

    getStatistics(): Promise<MonitoringEvent[]> {
        return fetch(`${this.backendUrl}/monitoring/events`)
            .then(response => response.json())
    }

    replay(item: MonitoringEvent) {
        return fetch(`${this.backendUrl}/monitoring/replay`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(item.event.incomingEvent),
        })
    }
}

export interface MonitoringEvent {
    date: Date,
    event: EventPayload
}

export interface EventPayload {
    type: string,
    incomingEvent: IncomingEventPayload
}

export interface IncomingEventPayload {
    type: string
    id: string
    replay?: string,
    date: Date
}