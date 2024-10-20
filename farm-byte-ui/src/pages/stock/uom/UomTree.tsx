import {useNavigate} from "react-router-dom";
import {useSelector, useDispatch} from "react-redux";
import {
    getLoading,
    getPageSize,
    getTotalElements,
    getTotalPages, searchUoms,
    selectUoms
} from "@/pages/stock/uom/UomSlice.ts";
import {useEffect, useState} from "react";
import {
    DEFAULT_DIRECTION_VALUE,
    DEFAULT_PAGE_VALUE, MAX_SIZE_VALUE
} from "@/constants/page.constant.ts";
import useDebounce from "@/hooks/useDebounce.tsx";
import {DEBOUNCE_TIMEOUT} from "@/constants/app.constant.ts";
import {Direction} from "@/constants/directionConstant.ts";
import {useTranslation} from "react-i18next";
import {ColumnDef} from "@tanstack/react-table";
import {UomModel} from "@/pages/stock/uom/UomModel.ts";
import {RootDispatch, RootState} from "@/Store.ts";
import DataTable from "@/components/DataTable.tsx";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {
    findUomCategories,
    selectUomCategoryById
} from "@/pages/stock/uom-category/UomCategorySlice.ts";
import {Checkbox} from "@/components/ui/checkbox.tsx";

const UomTree = () => {

    const uoms: Array<UomModel> = useSelector(selectUoms);
    const pageSize: number = useSelector(getPageSize);
    const totalElements = useSelector(getTotalElements);
    const totalPages = useSelector(getTotalPages);
    const isLoading = useSelector(getLoading);
    const dispatch = useDispatch<RootDispatch>();

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

    const columns: ColumnDef<UomModel>[] = [
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
            header: () => (<div className="text-start">{t("uom_tree_name")}</div>),
        },
        {
            accessorKey: "uomType",
            header: () => (<div className="text-start">{t("uom_tree_uom_type")}</div>),
            cell: ({row}) => (<div className="text-start capitalize">{row.original.uomType.toLocaleLowerCase()}</div>),
        },
        {
            accessorKey: "ratio",
            header: () => (<div className="text-left">{t("uom_tree_ratio")}</div>),
        },
        {
            accessorKey: "uomCategoryId",
            header: () => (<div className="text-left">{t("uom_tree_uom_category_id")}</div>),
            cell: ({row}) => (<div className="text-start">{uomCategoryByName(row.original.uomCategoryId)}</div>),
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
        dispatch(findUomCategories({...searchParam, size: MAX_SIZE_VALUE}));
    }, [dispatch]);

    useEffect(() => {
        dispatch(searchUoms({
            page: page,
            size: size,
            attribute: "name",
            direction: Direction.ASC,
            keyword: keyword
        }));
    }, [debounceKeyword, page, size]);

    const uomCategoryByName = (uomCategoryId: string): string => {
        // eslint-disable-next-line react-hooks/rules-of-hooks
        const uomCategory =  useSelector((state: RootState) => selectUomCategoryById(state, uomCategoryId));
        return uomCategory? uomCategory.name: '';
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

    const handleDelete = (rows: Array<UomModel>) => {
        console.log(rows); //TODO
    }

    const handleEdit = (row: UomModel) => {
        navigate(`/stock/uoms/details/${row.id}`);
    }

    const handleNew = () => {
        navigate('/stock/uoms/new');
    }

    const handleClear = () => {
        setKeyword('');
    }

    return (
        <div className="text-3xl text-primary min-h-full">
            <DataTable
                title={'uom_tree_title'}
                columns={columns} data={uoms}
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
}

export default UomTree;