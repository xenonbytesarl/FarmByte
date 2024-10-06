import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Input} from "@/components/ui/input.tsx";
import {useTranslation} from "react-i18next";
import {useSelector} from "react-redux";
import {
    createUomCategory,
    findUomCategoryById,
    getLoading,
    getMessage,
    selectUomCategoryById,
    updateUomCategory
} from "@/pages/inventory/uom-category/UomCategorySlice.ts";
import {RootState, store} from "@/Store.ts";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {UomCategoryModel} from "@/pages/inventory/uom-category/UomCategoryModel.ts";
import {useForm} from "react-hook-form";
import {FormModeType} from "@/shared/model/FormModeType.ts";
import {z as Zod} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {unwrapResult} from "@reduxjs/toolkit";
import {useToast} from "@/hooks/use-toast.ts";
import {Toaster} from "@/components/ui/toaster.tsx";
import {cn} from "@/lib/utils.ts";
import FormCrudButton from "@/components/FormCrudButton.tsx";
import {ToastType} from "@/shared/constant/globalConstant.ts";


const UomCategoryForm = () => {

    const {t} = useTranslation(['home']);

    const {uomCategoryId} = useParams();

    const {toast} = useToast();

    const uomCategory: UomCategoryModel = useSelector((state:RootState) => selectUomCategoryById(state, uomCategoryId));
    const isLoading: boolean = useSelector(getLoading);
    const message: string = useSelector(getMessage);

    const navigate = useNavigate();
    const [mode, setMode] = useState<FormModeType>(uomCategoryId? FormModeType.READ: FormModeType.CREATE);

    const UomCategorySchema = Zod.object({
        name: Zod.string().min(1, t('uom_category_form_name_required_message')),
    });
    const defaultValuesUomCategory: UomCategoryModel = {
      id: "",
      name: "",
      parentUomCategoryId: "",
      active: true
    };
    const { register, handleSubmit, formState, reset, getValues } = useForm<UomCategoryModel>({
        defaultValues: uomCategoryId? defaultValuesUomCategory: {...uomCategory},
        resolver: zodResolver(UomCategorySchema)
    });
    const {errors, isValid } = formState;

    useEffect(() => {
        if(!uomCategory && uomCategoryId) {
            console.log(uomCategoryId);
            store.dispatch(findUomCategoryById(uomCategoryId));
        }
    }, [store.dispatch, uomCategory]);

    useEffect(() => {
        if(uomCategory) {
            reset(uomCategory)
        }
    }, [uomCategory, reset]);

    const showToast = (variant: ToastType, message: string) => {
        toast({
            className: cn(
                'top-0 right-0 flex fixed md:max-w-[420px] md:top-4 md:right-4'
            ),
            variant: variant,
            title: "FarmByte",
            description: t(message),
        });
    }


    const onSubmit = () => {
        const uomCategoryFormValue: UomCategoryModel = getValues();
        if (uomCategoryFormValue.id) {
            store.dispatch(updateUomCategory({uomCategoryId: uomCategoryFormValue.id, uomCategory: uomCategoryFormValue}))
                .then(unwrapResult)
                .then(() => {
                    setMode(FormModeType.READ);
                    showToast("info", message);
                })
                .catch((error) => {
                    setMode(FormModeType.EDIT);
                   showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                })
        } else {
            store.dispatch(createUomCategory(uomCategoryFormValue))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    showToast("success", message);
                    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                    // @ts-expect-error
                    navigate(`/inventory/uom-categories/detail/${response.data.content.id}`);
                })
                .catch((error) => {
                    setMode(FormModeType.CREATE);
                    showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                });
        }
    }

    const onEdit = () => {
        setMode(FormModeType.EDIT);
    }

    const onCancel = () => {
        if(uomCategory) {
            reset(uomCategory);
            setMode(FormModeType.READ);
        } else {
            reset();
        }
    }

    const onCreate = () => {
        navigate('/inventory/uom-categories/new');
        setMode(FormModeType.CREATE);
        reset(defaultValuesUomCategory);
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)} noValidate>
            <Toaster />
            <Card className="">
                <CardHeader>
                    <CardTitle className="flex flex-row justify-start items-center text-primary gap-5">
                        <span onClick={() => navigate(`/inventory/uom-categories`)}
                              className="material-symbols-outlined text-3xl cursor-pointer">arrow_back</span>
                        <span className="text-2xl">{t(uomCategoryId? 'uom_category_form_edit_title': 'uom_category_form_new_title')}</span>
                    </CardTitle>
                    <CardDescription>
                    <span className="flex flex-row w-full m-5">
                        <FormCrudButton
                            mode={mode}
                            isLoading={isLoading}
                            isValid={isValid}
                            onEdit={onEdit}
                            onCancel={onCancel}
                            onCreate={onCreate}
                        />
                        <span className="flex flex-row justify-end items-center gap-3 w-6/12">
                        </span>
                    </span>
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <div className="grid w-full items-center gap-4">
                        <Input id="name" type="hidden" {...register("id")} />

                        <div className="flex flex-col space-y-1.5">
                            <Label htmlFor="name">{t('uom_category_form_name_label')}</Label>
                            <Input id="name" type="text" {...register("name")}
                                   disabled={mode === FormModeType.READ || isLoading}/>
                            <small className="text-red-500">{errors.name?.message}</small>
                        </div>
                    </div>
                </CardContent>
                <CardFooter className="flex justify-between py-5">
                </CardFooter>
            </Card>
        </form>
    );
};

export default UomCategoryForm;