import {
    ColumnDef,
    flexRender,
    getCoreRowModel,
    RowSelectionState,
    useReactTable
} from "@tanstack/react-table";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {useTranslation} from "react-i18next";
import {useState} from "react";
import {Input} from "@/components/ui/input.tsx";
import {DEFAULT_PAGE_SIZE_OPTIONS} from "@/constants/page.constant.ts";
import Spinner from "@/components/Spinner.tsx";



interface DataTableProps<TData, TValue> {
    columns: ColumnDef<TData, TValue>[];
    data: TData[],
    totalElements: number,
    totalPages: number,
    page: number,
    size: number,
    isLoading?: boolean,
    title: string,
    keyword: string,
    handlePageChange: (page: number) => void,
    handleSizeChange: (size: number) => void,
    handleNew: () => void,
    handleFilterChange: (keyword: string) => void,
    handleClear: () => void,
    handleDelete: (data: TData[]) => void,
}

const  DataTable = <TData, TValue>({columns, data, totalElements, totalPages, page, size, title,
            keyword, handlePageChange, isLoading, handleSizeChange, handleNew,  handleFilterChange, handleClear, handleDelete}: DataTableProps<TData, TValue>) => {

    const {t} = useTranslation(['home']);

    const [rowSelection, setRowSelection] = useState<RowSelectionState>({});

    const table = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
        manualPagination: true,
        manualSorting: true,
        manualFiltering: true,
        rowCount: totalElements,
        pageCount: totalPages,
        onRowSelectionChange: setRowSelection,
        state: {
            pagination: {
                pageIndex: page,
                pageSize: size,
            },
            rowSelection,
        }
    });


    return (
        <div>
            {isLoading ? <Spinner/> : ''}
            <div className="p-10">
                <Card>
                    <CardHeader>
                        <CardTitle className="flex flex-row justify-between items-center text-primary">
                            <p className="text-2xl">{t(title)}</p>
                            <div className="flex flex-row items-center justify-end gap-4">
                                <Button onClick={() => handleNew()} variant="default"
                                        className="flex flex-row justify-center items-center">
                                    <span className="material-symbols-outlined text-xl">add</span>
                                    <span>{t('tree_button_label_add')}</span>
                                </Button>
                                {
                                    table.getFilteredSelectedRowModel().rows.length > 0
                                    &&
                                    <Button
                                        onClick={() => handleDelete(table.getFilteredSelectedRowModel().rows.map(row => row.original))}
                                        variant="destructive" className="flex flex-row justify-center items-center">
                                        <span className="material-symbols-outlined text-xl">delete</span>
                                        <span>{t('tree_button_label_delete')}</span>
                                    </Button>
                                }
                            </div>
                        </CardTitle>
                        <CardDescription>
                <span className="relative flex flex-row justify-end items-center py-4">
                    <Input
                        placeholder={t('tree_search_place_holder')}
                        value={keyword}
                        onChange={(event) => handleFilterChange(event.target.value)}
                        className="max-w-lg rounded-full"
                    />
                    <span onClick={() => handleClear()}
                          className={`material-symbols-outlined absolute mr-2 cursor-pointer text-2xl}`}>{keyword.length > 0 ? 'close' : 'search'}</span>
                </span>
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="w-full justify-between items-center">
                            <div className="rounded-md border">
                                <Table>
                                    <TableHeader>
                                        {table.getHeaderGroups().map((headerGroup) => (
                                            <TableRow key={headerGroup.id}>
                                                {headerGroup.headers.map((header) => {
                                                    return (
                                                        <TableHead key={header.id}>
                                                            {header.isPlaceholder
                                                                ? null
                                                                : flexRender(
                                                                    header.column.columnDef.header,
                                                                    header.getContext()
                                                                )}
                                                        </TableHead>
                                                    )
                                                })}
                                            </TableRow>
                                        ))}
                                    </TableHeader>
                                    <TableBody>
                                        {
                                            table.getRowModel().rows?.length ? (
                                                table.getRowModel().rows.map((row) => (
                                                    <TableRow
                                                        /*onClick={() => handleEdit(row.original)}*/
                                                        key={row.id}
                                                        data-state={row.getIsSelected() && "selected"}
                                                        className={row.getIsSelected() ? 'selected' : ''}
                                                        onClick={row.getToggleSelectedHandler()}
                                                    >
                                                        {row.getVisibleCells().map((cell) => (
                                                            <TableCell key={cell.id}>
                                                                {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                                            </TableCell>
                                                        ))}
                                                    </TableRow>
                                                ))
                                            ) : (
                                                <TableRow>
                                                    <TableCell colSpan={columns.length}
                                                               className="h-24 text-center">
                                                        No results.
                                                    </TableCell>
                                                </TableRow>
                                            )
                                        }
                                    </TableBody>
                                </Table>
                            </div>
                        </div>
                    </CardContent>
                    <CardFooter className="flex items-center justify-between space-x-2 py-4">

                        <div className="w-full">
                            <select
                                className="text-base w-16 text-center"
                                value={table.getState().pagination.pageSize}
                                onChange={event => handleSizeChange(Number(event.target.value))}
                            >
                                {DEFAULT_PAGE_SIZE_OPTIONS.map(opt => (
                                    <option key={opt} value={opt}>
                                        {opt}
                                    </option>
                                ))}
                            </select>
                            <div className="flex flex-row justify-end items-center">
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => handlePageChange(0)}
                                    disabled={!table.getCanPreviousPage()}
                                >
                                    {'<<'}
                                </Button>
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => handlePageChange(table.getState().pagination.pageIndex - 1)}
                                    disabled={!table.getCanPreviousPage()}
                                >
                                    {'<'}
                                </Button>
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => handlePageChange(table.getState().pagination.pageIndex + 1)}
                                    disabled={!table.getCanNextPage()}
                                >
                                    {'>'}
                                </Button>
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => handlePageChange(totalPages - 1)}
                                    disabled={!table.getCanNextPage()}
                                >
                                    {'>>'}
                                </Button>
                                <p className="text-sm ml-8">
                                    {page == 0 ? size > totalElements ? totalElements : size : (page + 1) == totalPages ? totalElements : (page + 1) * size} of {totalElements}
                                </p>
                            </div>
                        </div>
                    </CardFooter>
                </Card>
            </div>
        </div>

    )
}

export default DataTable