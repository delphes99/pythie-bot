import {ConnectorStatusEnum} from "@/connector/common/status/connector.status.enum"

export enum StatusColor {
    transparent = "border-0",
    grey = "border-8 border-grey-800",
    red = "border-8 border-red-800",
    green = "border-8 border-green-800",
    orange = "border-8 border-orange-800",
    yellow = "border-8 border-yellow-800",
}

// eslint-disable-next-line @typescript-eslint/no-namespace
export namespace StatusColor {
    export function of(status: ConnectorStatusEnum): StatusColor {
        switch (status) {
            case ConnectorStatusEnum.unconfigured:
                return StatusColor.transparent
            case ConnectorStatusEnum.configured:
                return StatusColor.grey
            case ConnectorStatusEnum.connected:
                return StatusColor.green
            case ConnectorStatusEnum.inError:
                return StatusColor.red
            case ConnectorStatusEnum.connecting:
                return StatusColor.yellow
            case ConnectorStatusEnum.disconnecting:
                return StatusColor.yellow
            case ConnectorStatusEnum.mixed:
                return StatusColor.orange
            default:
                return StatusColor.transparent
        }
    }
}