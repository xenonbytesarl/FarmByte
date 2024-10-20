import {useState, useEffect} from "react";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {ColumnDef} from "@tanstack/react-table";
import {StockLocationModel} from "@/pages/stock/stock-location/StockLocationModel.ts";
import {
    findStockLocations,
    getLoading,
    getPageSize,
    getTotalElements,
    getTotalPages,
    searchStockLocations, selectStockLocationById,
    selectStockLocations
} from "@/pages/stock/stock-location/StockLocationSlice.ts";
import {DEFAULT_DIRECTION_VALUE, DEFAULT_PAGE_VALUE, MAX_SIZE_VALUE} from "@/constants/page.constant.ts";
import useDebounce from "@/hooks/useDebounce.tsx";
import {DEBOUNCE_TIMEOUT} from "@/constants/app.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {Checkbox} from "@/components/ui/checkbox.tsx";
import {RootState, store} from "@/Store.ts";
import {Direction} from "@/constants/directionConstant.ts";
import DataTable from "@/components/DataTable.tsx";

const StockLocationTree = () => {
    const stockLocations: Array<StockLocationModel> = useSelector(selectStockLocations);
    const pageSize: number = useSelector(getPageSize);
    const totalElements = useSelector(getTotalElements);
    const totalPages = useSelector(getTotalPages);
    const isLoading = useSelector(getLoading);
    const navigate = useNavigate();

    const [page, setPage] = useState<number>(DEFAULT_PAGE_VALUE);
    const [size, setSize] = useState<number>(pageSize);
    const [keyword, setKeyword] = useState<string>('');
    const debounceKeyword = useDebounce(keyword, DEBOUNCE_TIMEOUT );

    const [searchParam, setSearchParam] = useState<SearchParamModel>({
        page: DEFAULT_PAGE_VALUE,
        size: pageSize,
        attribute: "name",
        direction: DEFAULT_DIRECTION_VALUE,
        keyword: keyword
    });

    const {t} = useTranslation(['home']);

    const columns: ColumnDef<StockLocationModel>[] = [
        {
            id: "select",
            header: ({ table }) => (
                <Checkbox
                    checked={
                        table.getIsAllPageRowsSelected() ||
                        (table.getIsSomePageRowsSelected() && "indeterminate")
                    }
                    onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
                    aria-label="Select all"
                />
            ),
            cell: ({ row }) => (
                <Checkbox
                    checked={row.getIsSelected()}
                    onCheckedChange={(value) => row.toggleSelected(!!value)}
                    aria-label="Select row"
                />
            ),
            enableSorting: false,
            enableHiding: false,
        },
        {
            accessorKey: "name",
            header: () => (<div className="text-start">{t("stock_tree_name")}</div>),
        },
        {
            accessorKey: "type",
            header: () => (<div className="text-start">{t("stock_tree_type")}</div>),
            cell: ({row}) => (<div className="text-start capitalize">{row.original.type.toLocaleLowerCase()}</div>),
        },
        {
            accessorKey: "parentId",
            header: () => (<div className="text-left">{t("stock_tree_parent_id")}</div>),
            cell: ({row}) => (<div className="text-start">{parentName(row.original?.parentId)}</div>),
        },
        {
            accessorKey: "action",
            header: () => "",
            cell: ({row}) => (
                <div  className="flex flex-row justify-end items-center gap-4 text-end ">
                    <span  onClick={() => handleEdit(row.original)} className="material-symbols-outlined text-primary text-xl cursor-pointer">edit</span>
                    <span  onClick={() => handleEdit(row.original)} className="material-symbols-outlined text-destructive text-xl cursor-pointer">delete</span>
                </div>
            )
        }
    ];

    useEffect(() => {
        store.dispatch(findStockLocations({...searchParam, size: MAX_SIZE_VALUE}));
    }, [store.dispatch]);

    useEffect(() => {
        store.dispatch(searchStockLocations({
            page: page,
            size: size,
            attribute: "name",
            direction: Direction.ASC,
            keyword: keyword
        }));
    }, [debounceKeyword, page, size]);

    const parentName = (stockLocationId: string): string => {
        // eslint-disable-next-line react-hooks/rules-of-hooks
        const stock =  useSelector((state: RootState) => selectStockLocationById(state, stockLocationId));
        return stock? stock.name: '';
    }

    const handlePageChange = (page: number) => {
        setSearchParam({page: page, size: size, attribute: "name", direction: DEFAULT_DIRECTION_VALUE, keyword: keyword});
        //TODO add page in pageInfo in backend and manage page in store
        setPage(page);
    }

    const handleSizeChange = (size: number) => {
        setSearchParam({page: DEFAULT_PAGE_VALUE, size: size, attribute: "name", direction: DEFAULT_DIRECTION_VALUE, keyword: keyword});
        //TODO add page in pageInfo in backend and manage page in store
        setSize(size);
        setPage(DEFAULT_PAGE_VALUE);
    }

    const handleFilterChange = (keyword: string) => {
        setKeyword(keyword);
        setPage(DEFAULT_PAGE_VALUE);
    }

    const handleDelete = (rows: Array<StockLocationModel>) => {
        console.log(rows); //TODO
    }

    const handleEdit = (row: StockLocationModel) => {
        navigate(`/stock/stock-locations/details/${row.id}`);
    }

    const handleNew = () => {
        navigate('/stock/stock-locations/new');
    }

    const handleClear = () => {
        setKeyword('');
    }

    return (
        <div className="text-3xl text-primary min-h-full">
            <DataTable
                title={'stock_tree_title'}
                columns={columns} data={stockLocations}
                totalElements={totalElements}
                page={page}
                size={size}
                totalPages={totalPages}
                isLoading={isLoading}
                keyword={keyword}
                handlePageChange={handlePageChange}
                handleNew={handleNew}
                handleFilterChange={handleFilterChange}
                handleSizeChange={handleSizeChange}
                handleClear={handleClear}
                handleDelete={handleDelete}
            />

        </div>
    );
};

export default StockLocationTree;