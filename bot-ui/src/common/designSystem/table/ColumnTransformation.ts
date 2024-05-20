export const ColumnTransformation = {
    DATE: (dateStr: string) => {
        return new Date(dateStr).toLocaleString();
    }
}