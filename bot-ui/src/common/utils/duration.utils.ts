export class Duration {
    constructor(
        readonly hours: number,
        readonly minutes: number,
        readonly seconds: number,
    ) {
    }

    withHours(hours: number): Duration {
        return new Duration(hours, this.minutes, this.seconds);
    }

    withMinutes(minutes: number): Duration {
        return new Duration(this.hours, minutes, this.seconds);
    }

    withSeconds(seconds: number): Duration {
        return new Duration(this.hours, this.minutes, seconds);
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