
export interface Duration {
    hours: number;
    minutes: number;
    seconds: number;
}

export function parseDuration(duration: string): Duration {
    const regex = /PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?/g;
    const match = regex.exec(duration);
    if (match) {
        return {
            hours: parseInt(match[1]) || 0,
            minutes: parseInt(match[2]) || 0,
            seconds: parseInt(match[3]) || 0,
        }
    }

    return {
        hours: 0,
        minutes: 0,
        seconds: 0,
    }
}

export function formatDuration(duration: Duration): string {
    return `PT${duration.hours}H${duration.minutes}M${duration.seconds}S`;
}