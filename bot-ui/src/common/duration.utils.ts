export class Duration {
    constructor(
        public hours: number,
        public minutes: number,
        public seconds: number,
    ) {
    }
}

export function parseDuration(duration: string): Duration {
    const regex = /PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?/g;
    const match = regex.exec(duration);
    if (match) {
        return new Duration(parseInt(match[1]) || 0,
            parseInt(match[2]) || 0,
            parseInt(match[3]) || 0);
    }

    return new Duration(0, 0, 0);
}

export function formatDuration(duration: Duration): string {
    return `PT${duration.hours}H${duration.minutes}M${duration.seconds}S`;
}